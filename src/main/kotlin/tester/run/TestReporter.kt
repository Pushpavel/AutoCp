package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import tester.format.presentableString
import tester.spec.TestGroupSpec
import tester.spec.TestSpec
import tester.result.TestListener
import tester.result.ResultCode

class TestReporter(private val processHandler: ProcessHandler) : TestListener {

    override fun testGroupStarted(testGroupSpec: TestGroupSpec) {
        ServiceMessageBuilder
            .testSuiteStarted(testGroupSpec.name)
            .apply()
    }

    override fun testGroupFinished(testGroupSpec: TestGroupSpec) {
        ServiceMessageBuilder
            .testSuiteFinished(testGroupSpec.name)
            .apply()
    }

    override fun testStarted(testSpec: TestSpec) {
        ServiceMessageBuilder
            .testStarted(testSpec.name)
            .apply()

        testOutput(
            testSpec,
            "${"___".repeat(15)}\n" +
                    "${testSpec.name}\n" +
                    "${"___".repeat(15)}\n" +
                    "\n"
        )
    }

    override fun testFinished(testSpec: TestSpec) {
        ServiceMessageBuilder
            .testFinished(testSpec.name)
            .apply()
        logOutput("\n")
    }


    override fun testPassed(testSpec: TestSpec) {
        testOutput(testSpec, "\n" + ResultCode.CORRECT_ANSWER.presentableString() + "\n")
    }

    override fun testFailed(testSpec: TestSpec, resultCode: ResultCode) {
        ServiceMessageBuilder
            .testFailed(testSpec.name)
            .addAttribute("message", "\n" + resultCode.presentableString())
            .apply()
    }

    override fun testOutput(testSpec: TestSpec, output: String) {
        ServiceMessageBuilder
            .testStdOut(testSpec.name)
            .addAttribute("out", output)
            .apply()
    }

    override fun testError(testSpec: TestSpec, errorOutput: String) {
        ServiceMessageBuilder
            .testStdErr(testSpec.name)
            .addAttribute("out", errorOutput)
            .apply()
    }

    override fun logError(message: String) =
        processHandler.notifyTextAvailable(message + "\n", ProcessOutputTypes.STDERR)

    override fun logOutput(message: String) =
        processHandler.notifyTextAvailable(message + "\n", ProcessOutputTypes.STDOUT)

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}