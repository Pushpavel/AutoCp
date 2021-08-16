package tester

import tester.base.ProcessRunner
import tester.errors.ProcessRunnerErr
import tester.errors.Verdict
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess
import tester.utils.trimByLines

/**
 * [TreeTestingProcess] containing implementation for executing and generating
 * verdict for each testcases
 */
class TestcaseTreeTestingProcess(rootTestNode: TestNode, reporter: Listener) :
    TreeTestingProcess(rootTestNode, reporter) {

    /**
     * executes and produces verdict for a Testcase
     */
    override suspend fun executeLeaf(node: TestNode.Leaf, parent: TestNode.Group): ResultNode.Leaf {
        val process = node.processFactory.createProcess()
        val expectedOutput = node.expectedOutput.trimByLines()

        val verdict = try {
            val result = ProcessRunner.run(process, node.input, parent.timeLimit)
            val actualOutput = result.output.trimByLines()
            if (!actualOutput.contentEquals(expectedOutput))
                Verdict.WrongAnswer(expectedOutput, actualOutput, result.executionTime)
            else
                Verdict.CorrectAnswer(result.output, result.executionTime)
        } catch (e: Exception) {
            when (e) {
                is ProcessRunnerErr.TimeoutErr -> Verdict.TimeLimitErr(e.timeLimit)
                is ProcessRunnerErr.RuntimeErr -> Verdict.RuntimeErr(e.output, e.localizedMessage)
                else -> Verdict.InternalErr(e)
            }
        }

        return ResultNode.Leaf(node, verdict)
    }

    /**
     * Executes Testcases of a Group
     */
    override suspend fun executeGroup(node: TestNode.Group, parent: TestNode.Group?): ResultNode.Group {
        val children = node.children.map { processNode(it, node) }

        if (children.isEmpty())
            listener.testingProcessError(node.name + ": No Testcases Found\n")

        return ResultNode.Group(node, children)
    }
}