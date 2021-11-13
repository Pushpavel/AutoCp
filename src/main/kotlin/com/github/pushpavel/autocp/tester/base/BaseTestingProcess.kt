package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.common.errors.InternalErr

/**
 * [TestingProcess] that ensures single execution of this class
 *
 * [executeProcess] will be called only once
 */
abstract class BaseTestingProcess : TestingProcess {
    private var hasExecuted = false

    abstract suspend fun executeProcess()

    override suspend fun execute() {
        if (hasExecuted)
            throw InternalErr("BaseTestingProcess::execute is called twice")
        hasExecuted = true
        executeProcess()
    }
}