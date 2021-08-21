package tester.base

import com.intellij.execution.configurations.GeneralCommandLine
import database.models.SolutionFile
import settings.langSettings.model.BuildConfig
import tester.utils.splitCommandString

class ExecuteProcessFactory(private val execCommand: List<String>) : ProcessFactory {
    override fun createProcess(): Process {
        return GeneralCommandLine(execCommand).createProcess()
    }

    companion object {
        fun from(
            solutionFile: SolutionFile,
            buildConfig: BuildConfig
        ): ProcessFactory {
            val command = buildConfig.constructCommand(solutionFile.pathString)
            val commandList = splitCommandString(command)
            return ExecuteProcessFactory(commandList)
        }
    }
}