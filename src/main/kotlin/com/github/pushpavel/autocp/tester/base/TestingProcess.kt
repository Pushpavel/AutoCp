package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.settings.langSettings.model.BuildConfig

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
        fun testingProcessError(message: String)
    }
}