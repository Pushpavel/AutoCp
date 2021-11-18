package com.github.pushpavel.autocp.core.runner.runtool

import com.github.pushpavel.autocp.common.helpers.defaultScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.tester.TestcaseTreeTestingProcess
import com.github.pushpavel.autocp.tester.base.TestingProcess
import com.intellij.execution.process.NopProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * [ProcessHandler][com.intellij.execution.process.ProcessHandler] that executes [executeProcess]
 * which is not an actual [Process]
 */
class ProcessLikeHandler(private val executeProcess: suspend ProcessLikeHandler.() -> Unit) : NopProcessHandler() {

    private var scope = defaultScope()

    override fun startNotify() {
        if (isStartNotified) return

        super.startNotify()

        // launch the testing Process
        scope.launch {
            try {
                executeProcess()
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