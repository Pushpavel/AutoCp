package tester.base

import com.intellij.execution.configurations.GeneralCommandLine
import common.errors.Err.TesterErr.BuildErr
import config.AutoCpConfig
import settings.AutoCpSettings
import tester.utils.splitCommandString
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.pathString

class SolutionProcessFactory(private val executablePath: String) {

    fun createProcess(): Process {
        return GeneralCommandLine().withExePath(executablePath).createProcess()
    }

    companion object {
        suspend fun buildFromConfig(config: AutoCpConfig): SolutionProcessFactory {
            val settings = AutoCpSettings.instance
            val lang = settings.getLangWithId(config.solutionLangId)
                ?: throw IllegalStateException("Select Solution Language in Run Configuration \"${config.name}\"")

            @Suppress("BlockingMethodInNonBlockingContext")
            val tempDir = Files.createTempDirectory("AutoCp")

            if (!tempDir.exists())
                tempDir.toFile().mkdir()


            val outputPath = Paths.get(tempDir.pathString, config.name + ".exe")
            val command = lang.buildCommandString(config.solutionFilePath, outputPath.pathString)
            val commandList = splitCommandString(command)
            val buildProcess = GeneralCommandLine(commandList).createProcess()

            val results = ProcessRunner.run(buildProcess)

            if (results.error.isNotEmpty())
                throw BuildErr(results.error)

            return SolutionProcessFactory(outputPath.pathString)
        }
    }

}