package tester.run

import tester.process.ProcessLike
import tester.result.ProgramResult

abstract class ProgramExecutor(private val input: String) : ProcessLike {
    private var process: Process? = null
    override fun start() = throw NotImplementedError("use ProgramExecutor.execute() instead of ProgramExecutor.start()")

    abstract fun createProcess(): Process

    fun execute(): ProgramResult {
        if (process != null)
            throw IllegalStateException("ProgramExecutor.execute() must be called only once !")

        val process = createProcess()

        this.process = process

        var output: String
        var error: String
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