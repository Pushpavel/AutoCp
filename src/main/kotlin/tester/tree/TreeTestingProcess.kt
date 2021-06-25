package tester.tree

import tester.base.BaseTestingProcess
import tester.base.TestingProcess

abstract class TreeTestingProcess(private val rootTestNode: TestNode, protected val listener: Listener) :
    BaseTestingProcess() {

    override suspend fun executeProcess() {
        val rootResultNode = processNode(rootTestNode)
        // todo: do something with the result
    }

    protected suspend fun processNode(node: TestNode): ResultNode =
        when (node) {
            is TestNode.Group -> {
                listener.groupStart(node)
                val result = executeGroup(node)
                listener.groupFinish(result)
                result
            }
            is TestNode.Leaf -> {
                listener.leafStart(node)
                val result = executeLeaf(node)
                listener.leafFinish(result)
                result
            }
        }

    abstract suspend fun executeLeaf(node: TestNode.Leaf): ResultNode.Leaf
    abstract suspend fun executeGroup(node: TestNode.Group): ResultNode.Group


    interface Listener : TestingProcess.Listener {
        fun leafStart(node: TestNode.Leaf)
        fun leafFinish(node: ResultNode.Leaf)
        fun groupStart(node: TestNode.Group)
        fun groupFinish(node: ResultNode.Group)
    }
}