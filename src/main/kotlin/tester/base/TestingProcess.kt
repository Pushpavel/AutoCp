package tester.base

import com.intellij.openapi.Disposable
import common.errors.Result

/**
 * Control Interface implemented by every layer of the Testing Process.
 * Implementations should not directly implement this interface.but, extend [BaseTestingProcess]
 */
interface TestingProcess<out T> : Disposable {
    suspend fun execute(): Result<T>
    fun hasExecuted(): Boolean
}