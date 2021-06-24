package tester

import tester.base.ProcessRunner
import tester.models.ResultCode
import tester.tree.ResultNode
import tester.tree.TestNode
import tester.tree.TreeTestingProcess

class TestcaseTreeTestingProcess(rootTestNode: TestNode, reporter: Listener) :
    TreeTestingProcess(rootTestNode, reporter) {

    override suspend fun executeLeaf(node: TestNode.Leaf): ResultNode.Leaf {
        val process = node.processFactory.createProcess()
        val result = ProcessRunner.run(process)

        // todo: produce verdict

        return ResultNode.Leaf(node, ResultCode.CORRECT_ANSWER, result.output, result.error, result.executionTime)
    }

    override suspend fun executeGroup(node: TestNode.Group): ResultNode.Group {
        val children = node.children.map { processNode(it) }
        return ResultNode.Group(node, children)
    }
}