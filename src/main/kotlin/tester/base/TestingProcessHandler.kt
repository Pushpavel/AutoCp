package tester.base

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import common.errors.Err
import common.errors.presentableString
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tester.TestcaseTreeTestingProcess

/**
 * Implementation of ProcessHandler that handles [TestingProcess] which is not an actual [Process]
 */
abstract class TestingProcessHandler : NopProcessHandler() {

    private var testingProcessJob: Job? = null

    abstract suspend fun createTestingProcess(): TestcaseTreeTestingProcess?

    override fun startNotify() {
        if (isStartNotified) return

        super.startNotify()

        // launch the testing Process
        testingProcessJob = GlobalScope.launch {
            try {
                val process = createTestingProcess()
                process?.execute()
            } catch (e: Exception) {
                if (e is Err)
                    notifyTextAvailable(e.presentableString() + "\n", ProcessOutputTypes.STDERR)
                // Last hope for logging any errors in the testing Process
                notifyTextAvailable(e.stackTraceToString(), ProcessOutputTypes.STDERR)
            } finally {
                destroyProcess()
            }
        }
    }

    override fun destroyProcessImpl() {
        // notifyProcessTerminated on successfully cancelling testingProcessJob
        testingProcessJob?.let {
            it.cancel()

            GlobalScope.launch {
                // ignoring exceptions as it will be handled within the job itself
                it.join()
                notifyProcessTerminated(0)
            }

        } ?: notifyProcessTerminated(0)
    }
}