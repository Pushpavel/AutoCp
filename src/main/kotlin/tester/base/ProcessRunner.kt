package tester.base

import com.intellij.openapi.diagnostic.getOrLogException
import com.jetbrains.rd.util.measureTimeMillis
import common.errors.Err
import kotlinx.coroutines.*

object ProcessRunner {

    suspend fun run(process: Process, input: String = "") = withContext(Dispatchers.IO) {
        val result: CapturedResults

        if (!process.isAlive)
            throw Err.InternalErr("ProcessRunner<T>.run: Dead Process provided as argument")

//        launch {
//            // poll coroutine for cancellation or process.destroy
//            while (isActive) delay(10)
//
//            process.destroy()
//        }

        try {
            setInput(process, input)
            val executionTime = captureExecutionTimeBlocking(process)
            val (output, error) = getOutputAndError(process)
            result = CapturedResults(output, error, executionTime)
        } finally {
            process.destroy()
        }

        return@withContext result
    }

    private suspend fun captureExecutionTimeBlocking(process: Process) = coroutineScope {
        measureTimeMillis {
            //ignoring InterruptedException
            runCatching {
                launch {
                    while (this.isActive && process.isAlive) delay(1)
                    process.destroy()
                }

            }
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