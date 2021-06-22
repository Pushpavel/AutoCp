package tester.run

import com.intellij.execution.configurations.GeneralCommandLine
import tester.execute.ProgramExecutor

class ExecutableRunner(
    private val executablePath: String,
    input: String
) : ProgramExecutor(input) {
    override fun createProcess(): Process {
        return GeneralCommandLine().withExePath(executablePath).createProcess()
    }

}