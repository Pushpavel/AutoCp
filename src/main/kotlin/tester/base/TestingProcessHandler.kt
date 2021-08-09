package tester.base

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import common.errors.Err
import common.errors.presentableString
import common.helpers.defaultScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import tester.TestcaseTreeTestingProcess

/**
 * [ProcessHandler][com.intellij.execution.process.ProcessHandler] that handles [TestingProcess]
 * which is not an actual [Process]
 */
abstract class TestingProcessHandler : NopProcessHandler() {

    private var testingProcessJob: Job? = null

    abstract suspend fun createTestingProcess(): TestcaseTreeTestingProcess?

    private var scope: CoroutineScope? = null

    override fun startNotify() {
        if (isStartNotified) return

        super.startNotify()
        scope = defaultScope()

        // launch the testing Process
        testingProcessJob = scope?.launch {
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

            scope?.launch {
                // ignoring exceptions as it will be handled within the job itself
                it.join()
                notifyProcessTerminated(0)
            }

            scope?.cancel()

        } ?: notifyProcessTerminated(0)
    }
}