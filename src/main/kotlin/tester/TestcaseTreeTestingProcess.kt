package tester

import tester.base.ProcessRunner
import tester.judge.Judge
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

class TestcaseTreeTestingProcess(rootTestNode: TestNode, reporter: Listener) :
    TreeTestingProcess(rootTestNode, reporter) {

    override suspend fun executeLeaf(node: TestNode.Leaf): ResultNode.Leaf {
        val process = node.processFactory.createProcess()
        val result = ProcessRunner.run(process, node.input)

        val (verdict, verdictError) = Judge.produceVerdict(node, result)

        return ResultNode.Leaf(node, verdict, verdictError, result.output, result.error, result.executionTime)
    }

    override suspend fun executeGroup(node: TestNode.Group): ResultNode.Group {
        val children = node.children.map { processNode(it) }

        if (children.isEmpty())
            listener.testingProcessError(node.name + ": No Testcases Found\n")

        return ResultNode.Group(node, children)
    }
}