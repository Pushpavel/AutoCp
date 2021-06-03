package tester.run

import com.intellij.execution.configurations.GeneralCommandLine
import tester.process.ProcessLike
import tester.result.ProgramResult

class ProgramExecutor(
    private val executablePath: String,
    private val input: String
) : ProcessLike {

    private var process: Process? = null

    override fun start() = throw NotImplementedError("use ProgramExecutor.execute() instead of ProgramExecutor.start()")

    fun execute(): ProgramResult {
        if (process != null)
            throw IllegalStateException("ProgramExecutor.execute() must be called only once !")

        val process = GeneralCommandLine().withExePath(executablePath).createProcess()

        this.process = process

        var output = ""
        var error = ""
        // give input
        process.outputStream.use {
            it.write(input.toByteArray())
        }

        process.inputStream.use {
            output = it.bufferedReader().readText()
        }

        process.errorStream.use {
            error = it.bufferedReader().readText()
        }

        return ProgramResult(output, error)
    }

    override fun dispose() {
        process?.destroy()
    }
}