package tester.base

/**
 * This interface is used instead of an actual Process in [TestingProcessHandler].
 * Implementations should not directly implement this interface. but, extend [BaseTestingProcess]
 */
interface TestingProcess {
    suspend fun execute()
}