package tester.base

import common.helpers.awaitAsResult
import kotlinx.coroutines.*
import tester.errors.ProcessRunnerErr

/**
 * Simplifies execution of an Sub [Process]  by handling its input, output and error streams
 * this enables to run a process by just calling ProcessRunner.run which returns
 * the output and errors in CapturedResults Object
 */
object ProcessRunner {

    suspend fun run(process: Process, input: String = "", timeLimit: Long = Long.MAX_VALUE) = coroutineScope {

        val deferred = async(Dispatchers.IO) {
            if (!process.isAlive)
                throw ProcessRunnerErr.DeadProcessErr

            setInput(process, input)


            return@async try {
                val deferredOutput = readOutputAsync(process)
                val executionTime = monitorProcess(process, timeLimit)
                val output = deferredOutput.awaitAsResult()

                CapturedResults(
                    output.getOrDefault(""),
                    executionTime
                )
            } finally {
                process.destroy()
            }
        }

        return@coroutineScope deferred.await()
    }

    private fun setInput(process: Process, input: String) {
        process.outputStream.use {
            it.write(input.toByteArray())
        }
    }

    private suspend fun monitorProcess(process: Process, timeLimit: Long) = coroutineScope {
        val startTime = System.currentTimeMillis()

        try {
            withTimeout(timeLimit) {
                while (process.isAlive) ensureActive()
            }
        } catch (e: TimeoutCancellationException) {
            throw ProcessRunnerErr.TimeoutErr(timeLimit)
        }

        return@coroutineScope System.currentTimeMillis() - startTime
    }

    private fun CoroutineScope.readOutputAsync(process: Process) = async(Dispatchers.IO) {
        val output = async { process.inputStream.bufferedReader().readText() }
        val error = async { process.errorStream.bufferedReader().readText() }

        awaitAll(output, error).let {
            if (it[1].isNotEmpty())
                throw ProcessRunnerErr.RuntimeErr(it[0], it[1])
            it[0]
        }
    }

    data class CapturedResults(
        val output: String,
        val executionTime: Long = 0
    )
}