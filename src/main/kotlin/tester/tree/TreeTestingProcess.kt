package tester.tree

import tester.base.BaseTestingProcess

abstract class TreeTestingProcess(private val rootTestNode: TestNode) : BaseTestingProcess<Unit>() {

    override suspend fun executeProcess() {
        val rootResultNode = processNode(rootTestNode)
        // todo: do something with the result
    }

    private suspend fun processNode(node: TestNode): ResultNode =
        when (node) {
            is TestNode.Group -> executeGroup(node)
            is TestNode.Leaf -> executeLeaf(node)
        }

    abstract suspend fun executeLeaf(node: TestNode.Leaf): ResultNode.Leaf
    abstract suspend fun executeGroup(node: TestNode.Group): ResultNode.Group
}