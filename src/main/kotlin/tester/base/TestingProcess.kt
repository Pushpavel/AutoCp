package tester.base

import common.errors.Err
import settings.langSettings.model.BuildConfig

/**
 * This interface is used instead of an actual Process in [TestingProcessHandler].
 * Implementations should not directly implement this interface. but, extend [BaseTestingProcess]
 */
interface TestingProcess {
    suspend fun execute()

    interface Listener {
        fun commandReady(configName: String, buildConfig: BuildConfig)
        fun compileStart(configName: String, buildConfig: BuildConfig)
        fun compileFinish(result: Result<ProcessRunner.CapturedResults>)
        fun testingProcessStartErrored(error: Err)
        fun testingProcessError(message: String)
    }
}