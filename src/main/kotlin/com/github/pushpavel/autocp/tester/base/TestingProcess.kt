package com.github.pushpavel.autocp.tester.base

/**
 * This interface is used instead of an actual Process in [TestingProcessHandler].
 * Implementations should not directly implement this interface. but, extend [BaseTestingProcess]
 */
interface TestingProcess {
    suspend fun execute()

    interface Listener {
        fun commandReady(configName: String)
        fun compileStart(configName: String)
        fun compileFinish(result: Result<ProcessRunner.CapturedResults>)
        fun testingProcessError(message: String)
    }
}