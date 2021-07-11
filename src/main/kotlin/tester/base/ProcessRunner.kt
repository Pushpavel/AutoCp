package tester.base

import common.awaitAsResult
import common.errors.Err
import common.errors.Err.TesterErr.*
import kotlinx.coroutines.*
import java.io.InputStream

/**
 * Simplifies execution of an Sub [Process]  by handling its input, output and error streams
 * this enables to run a process by just calling ProcessRunner.run which returns
 * the output and errors in CapturedResults Object
 */
object ProcessRunner {

    suspend fun run(process: Process, input: String = "", timeLimit: Long = Long.MAX_VALUE) =
        withContext(Dispatchers.IO) {

            val result = runCatching {
                if (!process.isAlive)
                    throw Err.InternalErr("ProcessRunner<T>.run: Dead Process provided as argument")

                setInput(process, input)
                val deferredOutput = readOutputAsync(process)
                val executionTime = monitorProcess(process, timeLimit)
                val output = deferredOutput.awaitAsResult()

                CapturedResults(
                    output.getOrDefault(""),
                    executionTime
                )
            }

            process.destroy()

            return@withContext result
        }

    private fun setInput(process: Process, input: String) {
        process.outputStream.use {
            it.write(input.toByteArray())
        }
    }

    private suspend fun monitorProcess(process: Process, timeLimit: Long) = coroutineScope {
        val startTime = System.currentTimeMillis()

        withTimeout(timeLimit) {
            while (process.isAlive) ensureActive()
        }

        return@coroutineScope System.currentTimeMillis() - startTime
    }

    private fun CoroutineScope.readOutputAsync(process: Process) = async(Dispatchers.IO) {
        val output = async { process.inputStream.bufferedReader().readText() }
        val error = async { process.errorStream.bufferedReader().readText() }

        awaitAll(output, error).let {
            if (it[1].isNotEmpty())
                throw RuntimeErr(it[1])
            it[0]
        }
    }

    data class CapturedResults(
        val output: String,
        val executionTime: Long = 0
    )
}