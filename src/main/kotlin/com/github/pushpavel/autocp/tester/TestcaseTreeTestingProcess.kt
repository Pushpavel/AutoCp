package com.github.pushpavel.autocp.tester

import com.github.pushpavel.autocp.tester.errors.ProcessRunnerErr
import com.github.pushpavel.autocp.tester.errors.TestcaseJudgingErr
import com.github.pushpavel.autocp.tester.errors.Verdict
import com.github.pushpavel.autocp.tester.tree.ResultNode
import com.github.pushpavel.autocp.tester.tree.TestNode
import com.github.pushpavel.autocp.tester.tree.TreeTestingProcess
import com.github.pushpavel.autocp.tester.utils.trimByLines

/**
 * [TreeTestingProcess] containing implementation for executing and generating
 * verdict for each testcases
 */
class TestcaseTreeTestingProcess(rootTestNode: TestNode, reporter: Listener, private val isStress: Boolean) :
    TreeTestingProcess(rootTestNode, reporter) {

    /**
     * executes and produces verdict for a Testcase
     */
    override suspend fun executeLeaf(node: TestNode.Leaf, parent: TestNode.Group): ResultNode.Leaf {
        val expectedOutput = node.testcase.output?.trimByLines() ?: ""

        val verdict = try {
            if (parent.judge?.shouldUseJudge(node.testcase) == true) {
                parent.judge.judgeTestcase(node.testcase, parent.participant)
            } else {
                val performance = parent.participant
                    .setInput(node.testcase.input, parent.settings.inputFile)
                    .registerOutput("answer", parent.settings.outputFile)
                    .run(parent.settings.timeLimit)
                if (performance.exitCode != 0) {
                    Verdict.RuntimeErr(
                        performance["answer"] ?: "",
                        "Solution exited with invalid exit code ${performance.exitCode}"
                    )
                } else {
                    val answer = performance["answer"]?.trimByLines() ?: ""
                    if (!answer.contentEquals(expectedOutput))
                        Verdict.WrongAnswer(node.testcase.output, answer, performance.executionTime)
                    else
                        Verdict.CorrectAnswer(answer, performance.executionTime)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ProcessRunnerErr.TimeoutErr -> Verdict.TimeLimitErr(e.timeLimit)
                is ProcessRunnerErr.RuntimeErr -> Verdict.RuntimeErr(e.output, e.localizedMessage)
                is TestcaseJudgingErr.JudgeFailed -> Verdict.RuntimeErr(e.output, e.localizedMessage)
                else -> Verdict.InternalErr(e)
            }
        } finally {
            parent.participant.reset()
        }

        return ResultNode.Leaf(node, verdict)
    }

    /**
     * Executes Testcases of a Group
     */
    override suspend fun executeGroup(node: TestNode.Group, parent: TestNode.Group?): ResultNode.Group {
        val children = mutableListOf<ResultNode>()
        val it = node.children.iterator()
        while (it.hasNext()) {
            children.add(processNode(it.next(), node))
            if (isStress && !isSuccess(children.last()))
                break;
        }

        if (children.isEmpty())
            listener.testingProcessError(node.name + ": No Testcases Found\n")

        return ResultNode.Group(node, children)
    }

    private fun isSuccess(result: ResultNode): Boolean {
        return when (result) {
            is ResultNode.Leaf -> result.verdict is Verdict.CorrectAnswer
            is ResultNode.Group -> result.children.all { isSuccess(it) }
        }
    }

}