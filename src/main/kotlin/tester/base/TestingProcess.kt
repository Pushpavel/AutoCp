package tester.base

import common.errors.Result

/**
 * Control interface implemented by every layer of the Testing Process.
 * This interface is used instead of an actual Process in [TestingProcessHandler].
 * Implementations should not directly implement this interface. but, extend [BaseTestingProcess]
 */
interface TestingProcess<out T> {
    suspend fun execute(): Result<T>
}