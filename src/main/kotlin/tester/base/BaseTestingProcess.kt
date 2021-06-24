package tester.base

import common.errors.Err

/**
 * This ensures [executeProcess] will be called only once
 */
abstract class BaseTestingProcess : TestingProcess {
    private var hasExecuted = false


    abstract suspend fun executeProcess()

    override suspend fun execute() {
        if (hasExecuted)
            throw Err.InternalErr("BaseTestingProcess::execute is called twice")
        hasExecuted = true
        return executeProcess()
    }
}