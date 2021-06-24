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
import tester.TestcaseTreeTestingProcess

abstract class TestingProcessHandler : NopProcessHandler(), ProcessListener {

    private var testingProcessJob: Job? = null

    abstract suspend fun createTestingProcess(): TestcaseTreeTestingProcess?

    override fun startNotified(event: ProcessEvent) {
        testingProcessJob = GlobalScope.launch {
            try {
                val process = createTestingProcess()
                process?.execute()
            } catch (e: Exception) {
                // Last hope for logging any errors in the testing Process
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