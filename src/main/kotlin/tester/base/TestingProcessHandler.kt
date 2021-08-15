package tester.base

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import common.errors.Err
import common.errors.presentableString
import common.helpers.defaultScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import tester.TestcaseTreeTestingProcess

/**
 * [ProcessHandler][com.intellij.execution.process.ProcessHandler] that handles [TestingProcess]
 * which is not an actual [Process]
 */
abstract class TestingProcessHandler : NopProcessHandler() {

    abstract suspend fun createTestingProcess(): TestcaseTreeTestingProcess?

    private var scope = defaultScope()

    override fun startNotify() {
        if (isStartNotified) return

        super.startNotify()

        // launch the testing Process
        scope.launch {
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
        scope.cancel()
        notifyProcessTerminated(0)
    }
}