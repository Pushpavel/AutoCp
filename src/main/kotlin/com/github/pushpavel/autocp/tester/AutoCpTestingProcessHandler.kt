package com.github.pushpavel.autocp.tester

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.build.settings.LangNotConfiguredErr
import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.config.AutoCpConfig
import com.github.pushpavel.autocp.config.validators.SolutionFilePathErr
import com.github.pushpavel.autocp.config.validators.getValidSolutionFile
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.github.pushpavel.autocp.tester.base.ProcessFactory
import com.github.pushpavel.autocp.tester.base.TestingProcessHandler
import com.github.pushpavel.autocp.tester.base.TwoStepProcessFactory
import com.github.pushpavel.autocp.tester.tree.TestNode
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

/**
 * [TestingProcessHandler] implementation that creates a [TestcaseTreeTestingProcess] for execution
 */
class AutoCpTestingProcessHandler(private val config: AutoCpConfig) : TestingProcessHandler() {

    private val reporter = TreeTestingProcessReporter(this)

    override suspend fun createTestingProcess(): TestcaseTreeTestingProcess? {
        try {
            // get and validate SolutionFile from config
            val solutionFile = getValidSolutionFile(config.project, config.name, config.solutionFilePath)

            // validate lang
            val extension = Path(solutionFile.pathString).extension
            val lang = LangSettings.instance.langs[extension] ?: throw LangNotConfiguredErr(extension)

            // Build Executable from Solution File and Lang
            val processFactory = compileIntoProcessFactory(solutionFile, lang) ?: return null

            // Build Test tree
            val rootNode = solutionFileToTestNode(solutionFile, processFactory)

            // create a TestingProcess from the Problem and Test Tree
            return TestcaseTreeTestingProcess(rootNode, reporter)
        } catch (err: Exception) {
            reportTestingStartErr(err)
            return null
        }
    }

    private suspend fun compileIntoProcessFactory(
        solutionFile: SolutionFile,
        lang: Lang
    ): ProcessFactory? {
        if (lang.buildCommand != null)
            reporter.compileStart(config.name)
        else
            reporter.commandReady(config.name)

        val result = runCatching { TwoStepProcessFactory.from(solutionFile, lang) }

        if (lang.buildCommand != null)
            reporter.compileFinish(result.map { it.second!! })

        return result.getOrNull()?.first
    }


    private fun solutionFileToTestNode(solutionFile: SolutionFile, processFactory: ProcessFactory): TestNode {
        val leafNodes = solutionFile.testcases.map {
            TestNode.Leaf(it.name, it.input, it.output, processFactory)
        }

        return TestNode.Group(
            Path(solutionFile.pathString).nameWithoutExtension,
            solutionFile.timeLimit,
            leafNodes,
            processFactory
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