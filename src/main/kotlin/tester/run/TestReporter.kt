package tester.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import tester.spec.TestGroupSpec
import tester.spec.TestSpec
import tester.result.TestListener

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
    }

    override fun testFinished(testSpec: TestSpec) {
        ServiceMessageBuilder
            .testFinished(testSpec.name)
            .apply()
    }

    override fun testFailed(testSpec: TestSpec, errorMessage: String) {
        ServiceMessageBuilder
            .testFailed(testSpec.name)
            .addAttribute("message", errorMessage)
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

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}