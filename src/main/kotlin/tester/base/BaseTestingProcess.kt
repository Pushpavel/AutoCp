package tester.base

import common.errors.Err

/**
 * Base class that should be used to abstract different layers of execution in the Testing Process.
 * This ensures [executeProcess] will be called only once
 */
abstract class BaseTestingProcess<out T> : TestingProcess<T> {
    private var hasExecuted = false


    abstract suspend fun executeProcess(): T

    override suspend fun execute(): T {
        if (hasExecuted)
            throw Err.InternalErr("BaseTestingProcess::execute is called twice")
        hasExecuted = true
        return executeProcess()
    }
}