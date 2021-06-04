package tester.result

import tester.spec.TestGroupSpec
import tester.spec.TestSpec

interface TestListener {

    // testGroup execution
    fun testGroupStarted(testGroupSpec: TestGroupSpec)
    fun testGroupFinished(testGroupSpec: TestGroupSpec)

    // test execution
    fun testStarted(testSpec: TestSpec)
    fun testFinished(testSpec: TestSpec)

    // test events
    fun testPassed(testSpec: TestSpec)
    fun testFailed(testSpec: TestSpec, resultCode: ResultCode)
    fun testOutput(testSpec: TestSpec, output: String)
    fun testError(testSpec: TestSpec, errorOutput: String)

    // logging
    fun logOutput(message: String)
    fun logError(message: String)
}