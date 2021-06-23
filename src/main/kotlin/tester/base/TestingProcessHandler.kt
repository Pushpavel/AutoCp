package tester.base

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.util.Key
import common.errors.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class TestingProcessHandler : NopProcessHandler(), ProcessListener {

    private var testingProcessJob: Job? = null

    abstract fun createTestingProcess(): TestingProcess<Unit>

    override fun startNotified(event: ProcessEvent) {
        testingProcessJob = GlobalScope.launch {
            val process = createTestingProcess()

            try {
                process.execute()
            } catch (e: Exception) {
                notifyTextAvailable(e.stackTraceToString(), ProcessOutputTypes.STDERR)
                destroyProcess()
            }
        }
    }

    override fun processTerminated(event: ProcessEvent) {
        testingProcessJob?.cancel()
    }

    // ignored
    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) = Unit

}