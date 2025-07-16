package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.Program
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.github.pushpavel.autocp.tester.utils.createFile
import com.github.pushpavel.autocp.tester.utils.splitCommandString
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

/**
 * Factory Class for creating [Process] of created from [Lang.executeCommand]
 * [Lang.buildCommand] is while creating this Factory
 */
class TwoStepProcessFactory(private val workingDir: File, private val commandList: List<String>) : ProcessFactory {
    override fun createProcess(): Process {
        return GeneralCommandLine(commandList).withWorkDirectory(workingDir).createProcess()
    }

    companion object {
        suspend fun fromSolutionFile(
            project: Project,
            solutionFile: SolutionFile,
            lang: Lang,
            workingDir: File
        ): Pair<TwoStepProcessFactory, ProcessRunner.CapturedResults?> {
            workingDir.mkdirs()
            return fromPathString(project, lang, solutionFile.pathString, workingDir)
        }

        suspend fun fromProgram(
            project: Project,
            program: Program,
            fileName: String,
            workingDir: File
        ): Pair<TwoStepProcessFactory, ProcessRunner.CapturedResults?> {
            workingDir.mkdirs()

            val extension = program.languageExtension
            val file = createFile(workingDir, "$fileName.$extension", program.code ?: "")
            return fromPathString(project, program.lang, file.path.pathString, workingDir)
        }

        private suspend fun fromPathString(
            project: Project,
            lang: Lang,
            pathString: String,
            workingDir: File
        ): Pair<TwoStepProcessFactory, ProcessRunner.CapturedResults?> {
            val executeCommand = lang.constructExecuteCommand(project, pathString, workingDir.path.pathString)
            val executeCommandList = splitCommandString(executeCommand)

            var result: ProcessRunner.CapturedResults? = null
            if (lang.buildCommand != null) {
                val buildCommand = lang.constructBuildCommand(project, pathString, workingDir.path.pathString)
                val buildCommandList = splitCommandString(buildCommand)

                try {
                    val factory = TwoStepProcessFactory(workingDir, buildCommandList)
                    result = ProcessRunner(factory, workingDir).run()
                } catch (e: Exception) {
                    throw BuildErr(e, buildCommand)
                }
            }

            return Pair(TwoStepProcessFactory(workingDir, executeCommandList), result)
        }
    }
}