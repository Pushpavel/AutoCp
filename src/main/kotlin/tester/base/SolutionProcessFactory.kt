package tester.base

import com.intellij.execution.Platform
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
class SolutionProcessFactory(private val executablePath: String) : ProcessFactory {
    override fun createProcess(): Process {
        return GeneralCommandLine().withExePath(executablePath).createProcess()
    }

    companion object {
        /**
         * Builds an Executable and returns a factory for creating [Process]es of this executable
         */
        suspend fun from(
            solutionFile: SolutionFile,
            buildConfig: BuildConfig
        ): Pair<ProcessFactory, ProcessRunner.CapturedResults> {
            val tempDir = withContext(Dispatchers.IO) {
                @Suppress("BlockingMethodInNonBlockingContext")
                Files.createTempDirectory("AutoCp")
            }
            val executableExtension = if (Platform.current() == Platform.WINDOWS) ".exe" else ""

            val outputPath = Paths.get(
                tempDir.pathString,
                Path(solutionFile.pathString).nameWithoutExtension + executableExtension
            )

            val command = buildConfig.constructCommand(solutionFile.pathString, outputPath.pathString)
            val commandList = splitCommandString(command)
            val result: ProcessRunner.CapturedResults

            try {
                val buildProcess = GeneralCommandLine(commandList).createProcess()
                result = ProcessRunner.run(buildProcess)
            } catch (e: Exception) {
                throw BuildErr(e, command)
            }

            return Pair(SolutionProcessFactory(outputPath.pathString), result)
        }

    }
}

class BuildErr(val err: Exception, val command: String) : Exception()