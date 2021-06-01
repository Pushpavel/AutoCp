package run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import common.AutoCpProblem
import common.TestCase

class TestProcessReporter(private val processHandler: ProcessHandler) {

    fun startedTesting() {
        print("startedTesting\n")
        ServiceMessageBuilder
            .testsStarted()
            .apply()
    }

    fun testOutputLine(outputLine: String) {
        print("testOutputLine: $outputLine\n")
        ServiceMessageBuilder
            .testStdOut(outputLine)
            .apply()
    }

    fun testStarted(test: TestCase) {
        print("testStarted: ${test.getName()}\n")

        ServiceMessageBuilder
            .testStarted(test.getName())
            .apply()
    }

    fun testFailed(test: TestCase, errorMessage: String) {
        print("testFailed: ${test.getName()}, $errorMessage\n")

        ServiceMessageBuilder
            .testFailed(test.getName())
            .addAttribute("message", errorMessage)
            .apply()

//        ServiceMessageBuilder
//            .testStdErr(test.getName() + " Failed :(")
//            .apply()

    }

    fun testFinished(test: TestCase) {
        print("testFinished: ${test.getName()}\n")
        ServiceMessageBuilder
            .testFinished(test.getName())
            .apply()
    }

    fun problemStarted(problem: AutoCpProblem) {
        print("problemStarted: ${problem.name} \n")

        ServiceMessageBuilder
            .testSuiteStarted(problem.name)
            .apply()
    }

    fun problemFinished(problem: AutoCpProblem) {
        print("problemFinished: ${problem.name} \n")

        ServiceMessageBuilder
            .testSuiteFinished(problem.name)
            .apply()
    }

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}