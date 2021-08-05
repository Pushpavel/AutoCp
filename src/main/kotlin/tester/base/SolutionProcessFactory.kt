package tester.base

import com.intellij.execution.configurations.GeneralCommandLine
import common.errors.Err.TesterErr.BuildErr
import config.AutoCpConfig
import settings.langSettings.AutoCpLangSettings
import tester.utils.splitCommandString
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.pathString

/**
 * Factory Class for creating sub [Process]es of an executable created from buildFromConfig function
 */
class SolutionProcessFactory(private val executablePath: String) {

    fun createProcess(): Process {
        return GeneralCommandLine().withExePath(executablePath).createProcess()
    }

    companion object {
        /**
         * Creates an Executable from [AutoCpConfig] and returns a factory for creating sub [Process]es of this executable
         */
        suspend fun buildFromConfig(config: AutoCpConfig): SolutionProcessFactory {
            val buildConfig = AutoCpLangSettings.findBuildConfigById(config.buildConfigId!!) // fixme: buildConfigId can be null
                ?: throw BuildErr("Select a valid Build Configuration in Run Configuration \"${config.name}\"")

            @Suppress("BlockingMethodInNonBlockingContext")
            val tempDir = Files.createTempDirectory("AutoCp")

            if (!tempDir.exists())
                tempDir.toFile().mkdir()


            val outputPath = Paths.get(tempDir.pathString, config.name + ".exe")
            val command = buildConfig.constructBuildCommand(config.solutionFilePath, outputPath.pathString)
            val commandList = splitCommandString(command)
            val buildProcess = GeneralCommandLine(commandList).createProcess()

            val results = ProcessRunner.run(buildProcess)

            results.exceptionOrNull()?.let {
                throw BuildErr(it.stackTraceToString())
            }

            return SolutionProcessFactory(outputPath.pathString)
        }
    }

}