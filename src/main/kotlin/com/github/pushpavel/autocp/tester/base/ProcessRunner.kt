package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.common.helpers.awaitAsResult
import com.github.pushpavel.autocp.tester.errors.ProcessRunnerErr
import com.github.pushpavel.autocp.tester.utils.createFile
import com.github.pushpavel.autocp.tester.utils.readFile
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

/**
 * Simplifies execution of an Sub [Process]  by handling its input, output and error streams
 * this enables to run a process by just calling ProcessRunner.run which returns
 * the output and errors in CapturedResults Object
 */
class ProcessRunner(private val factory: ProcessFactory, private val workingDir: File?) {

    private val inputs: MutableMap<String?, String> = mutableMapOf()
    private val outputs: MutableMap<String, String?> = mutableMapOf()
    private val deletions: MutableSet<String> = mutableSetOf()
    private val outputListeners: MutableList<(String) -> Unit> = mutableListOf()

    private lateinit var process: Process
    private val cachedProgramInput = ByteArrayOutputStream()

    constructor(factory: ProcessFactory): this(factory, null)

    init {
        registerOutput("stdout", null)
    }

    suspend fun run(timeLimit: Long = Long.MAX_VALUE) = coroutineScope {
        process = factory.createProcess()
        for (input in inputs)
            if (input.key != null && workingDir != null)
                createFile(workingDir, input.key!!, input.value)
        cachedProgramInput.toString().ifEmpty { null }?.let {
            write(it)
            cachedProgramInput.reset()
        }

        val deferred = async(Dispatchers.IO) {
            if (!process.isAlive)
                throw ProcessRunnerErr.DeadProcessErr

            if (inputs.containsKey(null))
                process.outputStream.use {
                    write(inputs[null]!!)
                }
            return@async try {
                val deferredOutput = readOutputAsync(process)
                val executionTime = monitorProcess(process, timeLimit)
                val exitCode = process.exitValue()
                val stdout = deferredOutput.awaitAsResult().getOrNull()

                val out = mutableMapOf<String, String?>()
                for (output in outputs) {
                    out[output.key] = if (output.value == null) {
                        stdout
                    } else {
                        workingDir?.let { readFile(workingDir, output.value!!) }
                    }
                }
                if (workingDir != null)
                    for (deletion in deletions)
                        File(workingDir, deletion).takeIf { it.exists() }?.delete()

                CapturedResults(out, executionTime, exitCode)
            } finally {
                process.destroy()
            }
        }

        return@coroutineScope deferred.await()
    }

    fun write(s: String): Boolean {
        try {
            process.outputWriter().write(s)
            process.outputWriter().flush()
            return true
        } catch (e: IOException) {
            cachedProgramInput.write(s.toByteArray())
        }
        return false
    }

    fun terminateInput() = process.outputWriter().close()

    fun addOutputListener(listener: (String) -> Unit) {
        outputListeners.add(listener)
    }

    fun setInput(content: String, method: String?, deleteAfterwards: Boolean = true): ProcessRunner {
        inputs[method] = content
        if (method != null && deleteAfterwards)
            deletions.add(method)
        return this
    }

    fun registerOutput(name: String, method: String?, deleteAfterwards: Boolean = true): ProcessRunner {
        outputs[name] = method
        if (method != null && deleteAfterwards)
            deletions.add(name)
        return this
    }

    fun kill() = process.destroy()

    fun reset() {
        if (::process.isInitialized) {
            terminateInput()
            kill()
        }
        inputs.clear()
        outputs.clear()
        outputListeners.clear()
        cachedProgramInput.reset()
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
        val output = async {
            var output = ""
            process.inputStream.bufferedReader().forEachLine { line ->
                outputListeners.forEach { listener -> listener(line + '\n') }
                output += line + '\n'
            }
            output
        }
        val error = async { process.errorStream.bufferedReader().readText() }

        awaitAll(output, error).let {
            if (it[1].isNotEmpty())
                throw ProcessRunnerErr.RuntimeErr(it[0], it[1])
            it[0]
        }
    }

    data class CapturedResults(
        val outputs: Map<String, String?>,
        val executionTime: Long,
        val exitCode: Int
    ) {
        operator fun get(registeredOutput: String): String? {
            return outputs[registeredOutput]
        }
    }
}