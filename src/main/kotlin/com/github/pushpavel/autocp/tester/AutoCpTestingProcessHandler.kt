package com.github.pushpavel.autocp.tester

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.build.settings.LangNotConfiguredErr
import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpConfig
import com.github.pushpavel.autocp.config.validators.SolutionFilePathErr
import com.github.pushpavel.autocp.config.validators.getValidSolutionFile
import com.github.pushpavel.autocp.database.models.Program
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.github.pushpavel.autocp.database.models.Testcase
import com.github.pushpavel.autocp.tester.base.*
import com.github.pushpavel.autocp.tester.errors.TestGenerationErr
import com.github.pushpavel.autocp.tester.tree.TestNode
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

/**
 * [TestingProcessHandler] implementation that creates a [TestcaseTreeTestingProcess] for execution
 */
class AutoCpTestingProcessHandler(val project: Project, private val config: AutoCpConfig, private val isStress: Boolean) : TestingProcessHandler() {
    private val reporter = TreeTestingProcessReporter(this)

    override suspend fun createTestingProcess(): TestcaseTreeTestingProcess? {
        try {
            // Save the current solution file before compilation to ensure latest changes are compiled
            ApplicationManager.getApplication().invokeAndWait {
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(config.solutionFilePath)
                if (virtualFile != null) {
                    val document = FileDocumentManager.getInstance().getDocument(virtualFile)
                    if (document != null) {
                        FileDocumentManager.getInstance().saveDocument(document)
                        virtualFile.refresh(false, false)
                    }
                }
            }
            
            // get and validate SolutionFile from config
            val solutionFile = getValidSolutionFile(config.project, config.name, config.solutionFilePath)

            // validate lang
            val extension = Path(solutionFile.pathString).extension
            val lang = LangSettings.instance.langs[extension] ?: throw LangNotConfiguredErr(extension)

            val tempDir = withContext(Dispatchers.IO) {
                Files.createTempDirectory("AutoCp")
            }.toFile()

            // Build Executable from Solution File and Lang
            val processFactory = compileIntoProcessFactory(solutionFile, lang, tempDir) ?: return null

            // Build Test tree
            val rootNode = solutionFileToTestNode(solutionFile, processFactory, tempDir)

            // create a TestingProcess from the Problem and Test Tree
            return TestcaseTreeTestingProcess(rootNode, reporter, isStress)
        } catch (err: Exception) {
            reportTestingStartErr(err)
            return null
        }
    }

    private suspend fun compileIntoProcessFactory(
        solutionFile: SolutionFile,
        lang: Lang,
        workingDir: File
    ): ProcessFactory? {
        if (lang.buildCommand != null)
            reporter.compileStart(config.name)
        else
            reporter.commandReady(config.name)

        val result = runCatching {
            TwoStepProcessFactory.fromSolutionFile(project, solutionFile, lang, workingDir)
        }

        if (lang.buildCommand != null)
            reporter.compileFinish(result.map { it.second!! })

        return result.getOrNull()?.first
    }

    private suspend fun compileIntoProcessFactory(
        program: Program,
        fileName: String,
        workingDir: File
    ): ProcessFactory? = runCatching {
        program.code?.let { TwoStepProcessFactory.fromProgram(project, program, fileName, workingDir) }
    }.getOrNull()?.first

    private suspend fun solutionFileToTestNode(
        solutionFile: SolutionFile,
        processFactory: ProcessFactory,
        workingDir: File
    ): TestNode {
        val judge = compileIntoProcessFactory(
            solutionFile.judgeSettings.judgeProgram, "judge", File(workingDir, "judge")
        )?.let { Judge(ProcessRunner(it, File(workingDir, "judge")), solutionFile.judgeSettings) }
        val participant = ProcessRunner(processFactory, workingDir)
        val staticTestcases = solutionFile.testcases.map {
            TestNode.Leaf(it.name, Testcase(it.name, it.input, it.output))
        }.asSequence()
        val generatingTestcases = suspend {
            val generator = compileIntoProcessFactory(
                solutionFile.generator.generatorProgram, "gen", File(workingDir, "gen")
            )?.let { ProcessRunner(it, File(workingDir, "gen")) }
            if (generator == null)
                throw TestGenerationErr.GeneratorNotProvided()
            val correct = compileIntoProcessFactory(
                solutionFile.generator.correctProgram, "correct", File(workingDir, "correct")
            )?.let { ProcessRunner(it, File(workingDir, "correct")) }
            sequence {
                for (i in 1..solutionFile.generator.stressTestcaseAmount) {
                    var testInput: String
                    var correctOutput: String?
                    runBlocking {
                        val generatorResult = generator.setInput(i.toString() + '\n', null).run()
                        if (generatorResult.exitCode != 0)
                            throw TestGenerationErr.GeneratorFailed()
                        testInput = generatorResult.outputs["stdout"] ?: ""
                        correct?.setInput(testInput, solutionFile.judgeSettings.inputFile)
                        correct?.registerOutput("output", solutionFile.judgeSettings.outputFile)

                        val correctResult = correct?.run()
                        if (correctResult?.exitCode != 0)
                            throw TestGenerationErr.CorrectProgramFailed()
                        correctOutput = correctResult["output"]
                    }
                    val testcase = Testcase("Stress test, testcase $i", testInput, correctOutput)
                    yield(TestNode.Leaf(testcase.name, testcase))
                }
            }
        }
        val leafNodes = if (!isStress) {
            staticTestcases
        } else if (!solutionFile.generator.useStaticTestcases) {
            generatingTestcases()
        } else {
            sequenceOf(
                TestNode.Group(
                    "Static testcases",
                    staticTestcases,
                    solutionFile.judgeSettings,
                    solutionFile.generator.haltOnFailing,
                    participant,
                    judge
                ),
                TestNode.Group(
                    "Stress testing",
                    generatingTestcases(),
                    solutionFile.judgeSettings,
                    solutionFile.generator.haltOnFailing,
                    participant,
                    judge
                )
            )
        }

        return TestNode.Group(
            Path(solutionFile.pathString).nameWithoutExtension,
            leafNodes,
            solutionFile.judgeSettings,
            solutionFile.generator.haltOnFailing,
            participant,
            judge
        )
    }

    private fun reportTestingStartErr(err: Exception) {
        val message = when (err) {
            is SolutionFilePathErr -> R.strings.solutionFilePathErrMsg(err)
            is LangNotConfiguredErr -> R.strings.langNotConfiguredErrMsg(err)
            else -> throw err
        }

        reporter.testingProcessError(message)
    }
}