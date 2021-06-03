package tester.process

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.jetbrains.rd.util.printlnError

class ProcessLikeHandler(private var processLike: ProcessLike?) : NopProcessHandler() {

    init {
        addProcessListener(object : ProcessListener {
            override fun startNotified(event: ProcessEvent) {
                processLike?.start()
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
            processLike.start()
    }
}