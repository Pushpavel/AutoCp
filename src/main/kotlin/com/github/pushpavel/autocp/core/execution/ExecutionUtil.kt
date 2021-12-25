package com.github.pushpavel.autocp.core.execution

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.util.Key
import java.io.File
import kotlin.system.measureTimeMillis

object ExecutionUtil {
    data class Output(val processOutput: ProcessOutput, val executionTime: Long)

    fun execAndGetOutput(
        command: String,
        workingDir: File? = null,
        stdin: String? = null,
        timeoutInMilliseconds: Int? = null,
        progressIndicator: ProgressIndicator? = null,
        onTextAvailable: ((event: ProcessEvent, outputType: Key<*>) -> Unit)? = null
    ): Output {
        val commandLine = buildGeneralCommandLine(command, workingDir)
        val processHandler = CapturingProcessHandler(commandLine)
        if (stdin != null)
            processHandler.addProcessListener(object : ProcessAdapter() {
                override fun startNotified(event: ProcessEvent) {
                    processHandler.processInput.writer(commandLine.charset).use {
                        it.write(stdin)
                    }
                }

                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    onTextAvailable?.invoke(event, outputType)
                }
            })

        val processOutput: ProcessOutput
        val executionTime = measureTimeMillis {
            processOutput = when {
                timeoutInMilliseconds != null && progressIndicator != null -> {
                    processHandler.runProcessWithProgressIndicator(progressIndicator, timeoutInMilliseconds)
                }
                progressIndicator != null -> processHandler.runProcessWithProgressIndicator(progressIndicator)
                timeoutInMilliseconds != null -> processHandler.runProcess(timeoutInMilliseconds)
                else -> processHandler.runProcess()
            }
        }
        return Output(processOutput, executionTime)
    }

    private fun buildGeneralCommandLine(command: String, workingDir: File?): GeneralCommandLine {
        val commandList = splitCommandString(command)
        return GeneralCommandLine(commandList).withWorkDirectory(workingDir)
    }

    /**
     * splits single command string into the program and its arguments strings
     */
    private fun splitCommandString(command: String): List<String> {
        val commandList = Regex(""""(\\"|[^"])*?"|[^\s]+""").findAll(command).toList()
        return commandList.map { it.value.trim('"') }
    }

}
