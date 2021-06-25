package tester.base

import common.errors.Err

/**
 * This interface is used instead of an actual Process in [TestingProcessHandler].
 * Implementations should not directly implement this interface. but, extend [BaseTestingProcess]
 */
interface TestingProcess {
    suspend fun execute()

    interface Listener {
        fun testingProcessStartErrored(error: Err)
        fun testingProcessError(message: String)
    }
}