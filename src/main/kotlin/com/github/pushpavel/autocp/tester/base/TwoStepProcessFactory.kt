package com.github.pushpavel.autocp.tester.base

import com.intellij.execution.configurations.GeneralCommandLine
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.SolutionFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.github.pushpavel.autocp.settings.langSettings.model.BuildConfig
import com.github.pushpavel.autocp.tester.utils.splitCommandString
import java.io.File
import java.nio.file.Files

/**
 * Factory Class for creating [Process] of created from [BuildConfig.executeCommand]
 * [BuildConfig.buildCommand] is while creating this Factory
 */
class TwoStepProcessFactory(private val workingDir: File, private val commandList: List<String>) : ProcessFactory {
    override fun createProcess(): Process {
        return GeneralCommandLine(commandList).withWorkDirectory(workingDir).createProcess()
    }

    companion object {
        suspend fun from(
            solutionFile: SolutionFile,
            buildConfig: BuildConfig
        ): Pair<TwoStepProcessFactory, ProcessRunner.CapturedResults?> {
            val tempDir = withContext(Dispatchers.IO) {
                @Suppress("BlockingMethodInNonBlockingContext")
                Files.createTempDirectory("AutoCp")
            }.toFile()

            val executeCommand = buildConfig.constructExecuteCommand(solutionFile.pathString, tempDir.path.pathString)
            val executeCommandList = splitCommandString(executeCommand)

            var result: ProcessRunner.CapturedResults? = null
            if (buildConfig.buildCommand.isNotBlank()) {
                val buildCommand = buildConfig.constructBuildCommand(solutionFile.pathString, tempDir.path.pathString)
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