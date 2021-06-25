package tester

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.ServiceMessageBuilder.*
import common.errors.Err
import tester.format.presentableString
import tester.models.ResultCode
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

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
            ResultCode.CORRECT_ANSWER -> testStdOut(nodeName).addAttribute("out", "\n" + verdictString + "\n").apply()
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
        // todo: show a node in console to indicate error
    }

    override fun testingProcessError(message: String) {
        processHandler.notifyTextAvailable(message + "\n", ProcessOutputTypes.STDERR)
    }

    private fun ServiceMessageBuilder.apply() {
        println(this.toString() + "\n")
        processHandler.notifyTextAvailable(
            this.toString() + "\n", ProcessOutputTypes.STDOUT
        )
    }
}