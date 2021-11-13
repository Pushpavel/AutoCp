package com.github.pushpavel.autocp.tester.base

import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.github.pushpavel.autocp.common.helpers.defaultScope
import com.github.pushpavel.autocp.common.res.R
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import com.github.pushpavel.autocp.tester.TestcaseTreeTestingProcess

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
                createTestingProcess()?.execute()
            } catch (e: Exception) {
                // Last hope for logging any errors in the testing Process
                notifyTextAvailable(R.strings.fatalFileIssue(e), ProcessOutputTypes.STDERR)
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