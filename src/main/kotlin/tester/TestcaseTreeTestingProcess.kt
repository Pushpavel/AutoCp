package tester

import common.errors.Err
import common.errors.errOrNull
import tester.base.ProcessRunner
import tester.judge.Verdict
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess
import tester.utils.trimByLines

class TestcaseTreeTestingProcess(rootTestNode: TestNode, reporter: Listener) :
    TreeTestingProcess(rootTestNode, reporter) {

    override suspend fun executeLeaf(node: TestNode.Leaf, parent: TestNode.Group): ResultNode.Leaf {
        val process = node.processFactory.createProcess()

        val result = ProcessRunner.run(process, node.input, parent.timeLimit)

        val verdict: Verdict
        var verdictMessage = ""
        val error = result.errOrNull()
        if (error != null) {
            verdict = if (error is Err.TesterErr.TimeoutErr)
                Verdict.TIME_LIMIT_EXCEEDED
            else
                Verdict.RUNTIME_ERROR
        } else {
            val expectedOutput = node.expectedOutput.trimByLines()
            val actualOutput = result.getOrNull()!!.output.trimByLines()

            if (!actualOutput.contentEquals(expectedOutput)) {
                verdict = Verdict.WRONG_ANSWER
                verdictMessage = "Expected Output:\n${node.expectedOutput}"
            } else
                verdict = Verdict.CORRECT_ANSWER
        }

        return ResultNode.Leaf(
            node,
            verdict,
            verdictMessage,
            result.getOrNull()?.output ?: "",
            result.exceptionOrNull()?.stackTraceToString().takeIf { verdict != Verdict.TIME_LIMIT_EXCEEDED } ?: "",
            result.getOrNull()?.executionTime ?: parent.timeLimit
        )
    }

    override suspend fun executeGroup(node: TestNode.Group, parent: TestNode.Group?): ResultNode.Group {
        val children = node.children.map { processNode(it, node) }

        if (children.isEmpty())
            listener.testingProcessError(node.name + ": No Testcases Found\n")

        return ResultNode.Group(node, children)
    }
}