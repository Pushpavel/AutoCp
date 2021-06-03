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
    fun testFailed(testSpec: TestSpec, errorMessage: String)
    fun testOutput(testSpec: TestSpec, output: String)
    fun testError(testSpec: TestSpec, errorOutput: String)

    // logging
    fun logError(message: String)
}