package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.SolutionFile
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
        suspend fun from(
            project: Project,
            solutionFile: SolutionFile,
            lang: Lang
        ): Pair<TwoStepProcessFactory, ProcessRunner.CapturedResults?> {
            // TODO: reuse same temp dir to speed up compilations
            val tempDir = withContext(Dispatchers.IO) {
                @Suppress("BlockingMethodInNonBlockingContext")
                Files.createTempDirectory("AutoCp")
            }.toFile()

            val executeCommand = lang.constructExecuteCommand(project, solutionFile.pathString, tempDir.path.pathString)
            val executeCommandList = splitCommandString(executeCommand)

            var result: ProcessRunner.CapturedResults? = null
            if (lang.buildCommand != null) {
                val buildCommand = lang.constructBuildCommand(project, solutionFile.pathString, tempDir.path.pathString)
                val buildCommandList = splitCommandString(buildCommand)

                try {
                    val buildProcess = GeneralCommandLine(buildCommandList).withWorkDirectory(tempDir).createProcess()
                    result = ProcessRunner.run(buildProcess)
                } catch (e: Exception) {
                    throw BuildErr(e, buildCommand)
                }
            }

            return Pair(TwoStepProcessFactory(tempDir, executeCommandList), result)
        }
    }
}