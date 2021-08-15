package tester.base

import com.intellij.execution.configurations.GeneralCommandLine
import database.models.SolutionFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import settings.langSettings.model.BuildConfig
import tester.utils.splitCommandString
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension
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
         * Builds an Executable and returns a factory for creating [Process]es of this executable
         */
        suspend fun from(
            solutionFile: SolutionFile,
            buildConfig: BuildConfig
        ): SolutionProcessFactory {
            val tempDir = withContext(Dispatchers.IO) {
                @Suppress("BlockingMethodInNonBlockingContext")
                Files.createTempDirectory("AutoCp")
            }

            // fixme: executable extension must be dependent on OS
            val outputPath = Paths.get(tempDir.pathString, Path(solutionFile.pathString).nameWithoutExtension + ".exe")
            val command = buildConfig.constructBuildCommand(solutionFile.pathString, outputPath.pathString)
            val commandList = splitCommandString(command)
            try {
                val buildProcess = GeneralCommandLine(commandList).createProcess()
                ProcessRunner.run(buildProcess)
            } catch (e: Exception) {
                throw BuildErr(e, solutionFile, buildConfig)
            }

            return SolutionProcessFactory(outputPath.pathString)
        }

    }
}

class BuildErr(err: Exception, val solutionFile: SolutionFile, val buildConfig: BuildConfig) : Exception()