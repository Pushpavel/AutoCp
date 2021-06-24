package tester.base

import com.jetbrains.rd.util.measureTimeMillis
import common.errors.Err
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProcessRunner<T> {

    suspend fun run(process: Process, input: String = "") = withContext(Dispatchers.IO) {
        val result: CapturedResults

        if (!process.isAlive)
            throw Err.InternalErr("ProcessRunner<T>.run: Dead Process provided as argument")

        try {
            setInput(process, input)
            val executionTime = captureExecutionTime(process)
            val (output, error) = getOutputAndError(process)
            result = CapturedResults(output, error, executionTime)
        } finally {
            process.destroy()
        }

        return@withContext result
    }

    private suspend fun captureExecutionTime(process: Process): Long = withContext(Dispatchers.IO) {
        return@withContext measureTimeMillis {
            //ignoring InterruptedException
            runCatching { process.waitFor() }
        }
    }

    private fun setInput(process: Process, input: String) {
        process.outputStream.use {
            it.write(input.toByteArray())
        }
    }

    private fun getOutputAndError(process: Process): Pair<String, String> {
        val output: String
        val error: String

        process.inputStream.use { output = it.bufferedReader().readText() }
        process.errorStream.use { error = it.bufferedReader().readText() }

        return Pair(output, error)
    }

    data class CapturedResults(
        val output: String = "",
        val error: String = "",
        val executionTime: Long = 0
    )
}