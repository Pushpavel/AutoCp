package com.github.pushpavel.autocp.tester

import com.github.pushpavel.autocp.common.errors.NoReachErr
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.tester.base.BuildErr
import com.github.pushpavel.autocp.tester.base.ProcessRunner
import com.github.pushpavel.autocp.tester.tree.ResultNode
import com.github.pushpavel.autocp.tester.tree.TestNode
import com.github.pushpavel.autocp.tester.tree.TreeTestingProcess
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.ServiceMessageBuilder.*

/**
 * Class that abstracts formatting and sending output to the console and the TestRunner UI
 */
class TreeTestingProcessReporter(private val processHandler: ProcessHandler) : TreeTestingProcess.Listener {
    override fun leafStart(node: TestNode.Leaf) {
        testStarted(node.name).apply()
        testStdOut(node.name).addAttribute(
            "out",
            "${"___".repeat(5)}[ ${node.name} INPUT ]${"___".repeat(5)}\n" +
            node.testcase.input + '\n' +
            "${"___".repeat(5)}[ ${node.name} OUTPUT]${"___".repeat(5)}\n"
        ).apply()
    }

    override fun leafFinish(node: ResultNode.Leaf) {
        val nodeName = node.sourceNode.name
        if (node.verdict is com.github.pushpavel.autocp.tester.errors.Verdict.CorrectAnswer) {
            testStdOut(nodeName).addAttribute(
                "out",
                node.verdict.output + '\n' + R.strings.verdictOneLine(node.verdict) + '\n' +
                (node.verdict.comment?.trim()?.let { "judge's comment: $it\n" } ?: "")
            ).apply()

            testFinished(nodeName)
                .addAttribute("duration", node.verdict.executionTime.toString())
                .apply()

            return
        }

        when (node.verdict) {
            is com.github.pushpavel.autocp.tester.errors.Verdict.WrongAnswer -> {
                testStdOut(nodeName).addAttribute("out", node.verdict.actualOutput + '\n').apply()

                testFailed(nodeName)
                    .addAttribute("message",
                        R.strings.verdictOneLine(node.verdict) + '\n' +
                        (node.verdict.comment?.trim()?.let { "judge's comment: $it\n" } ?: "")
                    )
                    .addAttribute("type", "comparisonFailure")
                    .addAttribute("actual", node.verdict.actualOutput)
                    .addAttribute("expected", node.verdict.expectedOutput)
                    .apply()

                testFinished(nodeName)
                    .addAttribute("duration", node.verdict.executionTime.toString())
                    .apply()
            }
            is com.github.pushpavel.autocp.tester.errors.Verdict.RuntimeErr -> {

                testStdOut(nodeName)
                    .addAttribute("out", node.verdict.output + '\n')
                    .apply()

                testFailed(nodeName)
                    .addAttribute("message", R.strings.verdictOneLine(node.verdict))
                    .addAttribute("details", node.verdict.errMsg)
                    .apply()

                testFinished(nodeName)
                    .apply()
            }
            is com.github.pushpavel.autocp.tester.errors.Verdict.TimeLimitErr -> {
                testFailed(nodeName)
                    .addAttribute("message", R.strings.verdictOneLine(node.verdict))
                    .apply()

                testFinished(nodeName)
                    .addAttribute("duration", node.verdict.timeLimit.toString())
                    .apply()
            }
            is com.github.pushpavel.autocp.tester.errors.Verdict.InternalErr -> {
                testFailed(nodeName)
                    .addAttribute("message", R.strings.verdictOneLine(node.verdict))
                    .addAttribute("details", R.strings.defaultFileIssue(node.verdict.err))
                    .apply()

                testFinished(nodeName)
                    .apply()
            }
            else -> throw NoReachErr
        }
    }

    override fun groupStart(node: TestNode.Group) {
        testSuiteStarted(node.name).apply()
    }

    override fun groupFinish(node: ResultNode.Group) {
        testSuiteFinished(node.sourceNode.name).apply()
    }

    override fun commandReady(configName: String) {
        processHandler.notifyTextAvailable(
            R.strings.commandReadyMsg(configName) + "\n",
            ProcessOutputTypes.STDOUT
        )
    }

    override fun compileStart(configName: String) {
        processHandler.notifyTextAvailable(
            R.strings.startCompilingMsg(configName) + "\n",
            ProcessOutputTypes.STDOUT
        )
    }

    override fun compileFinish(result: Result<ProcessRunner.CapturedResults>) {
        when {
            result.isSuccess -> {
                val r = result.getOrThrow()
                val msg = R.strings.compileSuccessMsg(r.outputs["stdout"] ?: "", r.executionTime)
                processHandler.notifyTextAvailable(msg + "\n", ProcessOutputTypes.STDOUT)
            }
            result.isFailure -> {
                val msg = when (val e = result.exceptionOrNull()!!) {
                    is BuildErr -> R.strings.buildErrMsg(e)
                    else -> throw e
                }
                processHandler.notifyTextAvailable(msg + "\n", ProcessOutputTypes.STDERR)
            }
        }
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