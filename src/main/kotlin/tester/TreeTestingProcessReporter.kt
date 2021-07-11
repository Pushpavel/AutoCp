package tester

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.ServiceMessageBuilder.*
import common.errors.Err
import common.errors.presentableString
import tester.judge.Verdict
import tester.judge.Verdict.Companion.presentableString
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

/**
 * Class that abstracts formatting and sending output to the console and the TestRunner UI
 */
class TreeTestingProcessReporter(private val processHandler: ProcessHandler) : TreeTestingProcess.Listener {
    override fun leafStart(node: TestNode.Leaf) {
        testStarted(node.name).apply()
        testStdOut(node.name).addAttribute(
            "out",
            "${"___".repeat(5)}[ ${node.name} ]${"___".repeat(5)}\n"
        ).apply()
    }

    override fun leafFinish(node: ResultNode.Leaf) {
        val nodeName = node.sourceNode.name
        if (node.output.isNotEmpty())
            testStdOut(nodeName)
                .addAttribute("out", node.output.let {
                    if (it.endsWith('\n')) it
                    else it + '\n'
                })
                .apply()

        if (node.error.isNotEmpty())
            testStdErr(nodeName)
                .addAttribute("out", node.error)
                .addAttribute("message", node.verdict.presentableString())
                .apply()

        val verdictString = node.verdict.presentableString()
        when (node.verdict) {
            Verdict.CORRECT_ANSWER -> testStdOut(nodeName).addAttribute("out", "\n" + verdictString + "\n\n").apply()
            else -> {
                if (node.verdictError.isNotEmpty())
                    testStdErr(nodeName)
                        .addAttribute("out", node.verdictError)
                        .apply()

                testFailed(nodeName).addAttribute("message", verdictString + "\n").apply()
            }
        }

        testFinished(nodeName)
            .addAttribute("duration", node.executionTime.toString())
            .apply()
    }

    override fun groupStart(node: TestNode.Group) {
        testSuiteStarted(node.name).apply()
    }

    override fun groupFinish(node: ResultNode.Group) {
        testSuiteFinished(node.sourceNode.name).apply()
    }

    override fun testingProcessStartErrored(error: Err) {
        testsStarted()
        processHandler.notifyTextAvailable(
            error.presentableString() + "\n", ProcessOutputTypes.STDERR
        )

        processHandler.notifyTextAvailable(error.stackTraceToString(), ProcessOutputTypes.STDERR)
    }

    override fun testingProcessError(message: String) {
        processHandler.notifyTextAvailable(message + "\n", ProcessOutputTypes.STDERR)
    }

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}