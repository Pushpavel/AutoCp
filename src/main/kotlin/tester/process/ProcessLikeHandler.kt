package tester.process

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.jetbrains.rd.util.printlnError
import com.jetbrains.rd.util.string.PrettyPrinter
import com.jetbrains.rd.util.string.print

class ProcessLikeHandler(private var processLike: ProcessLike?) : NopProcessHandler() {

    init {
        addProcessListener(object : ProcessListener {
            override fun startNotified(event: ProcessEvent) {
                startProcessLike()
            }

            override fun processTerminated(event: ProcessEvent) {
                processLike?.let {
                    Disposer.dispose(it)
                }
            }

            // ignored
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) = Unit
        })
    }

    fun attachProcessLike(processLike: ProcessLike) {
        if (this.processLike != null) {
            printlnError("ProcessLike already attached")
            return
        }

        this.processLike = processLike
        if (isStartNotified)
            startProcessLike()
    }

    private fun startProcessLike() {
        runCatching {
            processLike?.start()
        }.onFailure {
            notifyTextAvailable(it.stackTraceToString(), ProcessOutputTypes.STDERR)
            destroyProcess()
        }
    }
}