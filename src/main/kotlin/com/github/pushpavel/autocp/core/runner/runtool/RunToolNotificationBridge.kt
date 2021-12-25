package com.github.pushpavel.autocp.core.runner.runtool

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.core.execution.BuildOutput
import com.github.pushpavel.autocp.core.execution.BuildSolutionErr
import com.github.pushpavel.autocp.core.persistance.storables.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcase
import com.github.pushpavel.autocp.core.runner.judge.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import kotlin.io.path.Path
import kotlin.io.path.name

class RunToolNotificationBridge(private val processHandler: ProcessHandler) : JudgingProcessListener {
    override fun onBuildingStarted(solutionPath: String, lang: Lang, buildCommand: String) {
        val name = Path(solutionPath).name
        stdout("Building \"$name\" ...")
    }

    override fun onTestingStarted(solution: Solution, testcases: List<Testcase>, buildOutput: BuildOutput) {
        if (buildOutput.buildCommand != null)
            stdout("DONE!\n")
        ServiceMessageBuilder.testsStarted().apply()
    }

    override fun onTestingFailed(err: JudgeErr) {
        if (err is JudgeErr.BuildErr)
            stdErr("FAILED!\n")

        when (err) {
            is JudgeErr.BuildErr -> {
                when (val e = err.error) {
                    is BuildSolutionErr.BuildFailure -> {
                        stdErr("Build Command -----\n")
                        stdErr(e.buildOutput.buildCommand!! + "\n")
                        stdErr("-----\n")
                        stdErr("Error Message -----\n")
                        stdErr("${e.output.processOutput.stderr}\n")
                        stdErr("-----\n")
                    }
                    is BuildSolutionErr.InvalidPath -> {
                        stdErr("Invalid Path -----\n")
                        stdErr(e.solutionPathString + "\n")
                        stdErr("-----\n")
                        stdErr("This path is not formatted correctly or a valid file may not exists at that path\n")
                    }
                    is BuildSolutionErr.NoLang -> {
                        stdErr("No Language Configured -----\n")
                        stdErr(e.solutionPathString + "\n")
                        stdErr("-----\n")
                        stdErr("\"${e.extension}\" does not have commands configured in Tools > AutoCp > Languages\n")
                    }
                }
            }
            is JudgeErr.NoSolution -> {
                stdErr("No Testcases Found -----\n")
                stdErr(err.solutionPathString + "\n")
                stdErr("-----\n")
                stdErr("Enable this file to work with AutoCp using Testcases toolWindow panel\n")
            }
            is JudgeErr.NoTestcases -> {
                stdErr("No Testcases Found -----\n")
                stdErr(err.solutionPathString + "\n")
                stdErr("-----\n")
                stdErr("Add Testcases to use AutoCp with this file\n")
            }
            is JudgeErr.UnknownErr -> {
                stdErr("Unknown Error -----\n")
                stdErr(err.error.localizedMessage + "\n")
                stdErr("-----\n")
                stdErr(R.strings.fileIssue + "\n")
            }
        }
    }


    override fun onTestGroupStarted(tests: TestGroupNode) {
        ServiceMessageBuilder.testSuiteStarted(tests.name).apply()
        for (testNode in tests.testNodes)
            ServiceMessageBuilder.testStarted(testNode.name)
                .addAttribute("flowId", testNode.name)
                .apply()
    }

    override fun onTestGroupFinished(results: ResultGroupNode) {
        ServiceMessageBuilder.testSuiteFinished(results.testGroupNode.name).apply()
    }

    override fun onTestNodeStarted(testNode: TestNode) {
        testStdOut(testNode.name, "---- [${testNode.name}] ----\n")
    }

    override fun onTestNodeStdOut(testNode: TestNode, value: String) {
        testStdOut(testNode.name, value)
    }

    override fun onTestNodeStdErr(testNode: TestNode, value: String) {
        testStdErr(testNode.name, value)
    }

    override fun onTestNodeFinished(result: ResultNode) {
        if (result.verdict != Verdict.CORRECT_ANSWER) {
            var msg = ServiceMessageBuilder.testFailed(result.source.name)
                .addAttribute("flowId", result.source.name)
                .addAttribute("message", R.strings.verdictOneLine(result.verdict))
            if (result.verdict == Verdict.WRONG_ANSWER) {
                msg = msg.addAttribute("type", "comparisonFailure")
                    .addAttribute("expected", result.source.expectedOutput)
                    .addAttribute("actual", result.output.processOutput.stdout)
            }
            msg.apply()
        } else
            testStdOut(result.source.name, R.strings.verdictOneLine(result.verdict))

        ServiceMessageBuilder
            .testFinished(result.source.name)
            .addAttribute("flowId", result.source.name)
            .addAttribute("duration", result.output.executionTime.toString()).apply()
    }

    override fun onTestingFinished() = stdout("Testing DONE!\n")

    /* Utils */

    private fun ServiceMessageBuilder.apply() {
        processHandler.notifyTextAvailable(this.toString() + "\n", ProcessOutputTypes.STDOUT)
    }

    private fun testStdOut(name: String, value: String) {
        ServiceMessageBuilder
            .testStdOut(name)
            .addAttribute("flowId", name)
            .addAttribute("out", value)
            .apply()
    }

    private fun testStdErr(name: String, value: String) {
        ServiceMessageBuilder
            .testStdErr(name)
            .addAttribute("flowId", name)
            .addAttribute("out", value)
            .apply()
    }

    private fun stdout(value: String) {
        processHandler.notifyTextAvailable(value, ProcessOutputTypes.STDOUT)
    }

    private fun stdErr(value: String) {
        processHandler.notifyTextAvailable(value, ProcessOutputTypes.STDERR)
    }

}
