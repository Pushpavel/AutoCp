package run

import com.intellij.openapi.Disposable
import common.TestCase

class RunTestCase(runCommand: String, testcase: TestCase, reporter: TestProcessReporter) : Disposable {

    private val process = Runtime.getRuntime().exec(runCommand)

    init {
        reporter.testStarted(testcase)

        val reader = process.inputStream.bufferedReader()

        reader.useLines { lines ->
            val expectedOutputLines = testcase.output.split('\n').filter { it.isNotEmpty() }.iterator()
            for (outputLine in lines) {
                if (outputLine.isNotEmpty()) {

                    reporter.testOutputLine(outputLine)
                    if (expectedOutputLines.hasNext()) {
                        val expectedOutputLine = expectedOutputLines.next()
                        if (outputLine != expectedOutputLine) {
                            reporter.testFailed(
                                testcase,
                                "^ Expected Output: $expectedOutputLine\n"
                            )
                            dispose()
                        }
                    } else {
                        reporter.testFailed(
                            testcase,
                            "^ Output Not Expected\n"
                        )
                        dispose()
                    }
                }
            }

            if (expectedOutputLines.hasNext()) {
                var errorMessage = "Output Finished But Expected Output:\n"
                expectedOutputLines.forEachRemaining {
                    errorMessage += it + "\n"
                }
                reporter.testFailed(testcase, errorMessage)
                dispose()
            }
        }
        reporter.testFinished(testcase)
        dispose()
    }

    override fun dispose() {
        process.destroy()
    }
}