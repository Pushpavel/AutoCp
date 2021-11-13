package com.github.pushpavel.autocp.tester.tree

import com.github.pushpavel.autocp.tester.base.BaseTestingProcess
import com.github.pushpavel.autocp.tester.base.TestingProcess

/**
 * [TestingProcess] that provides abstract functions for execution
 * to take place like a tree traversal
 */
abstract class TreeTestingProcess(private val rootTestNode: TestNode, protected val listener: Listener) :
    BaseTestingProcess() {

    override suspend fun executeProcess() {
        processNode(rootTestNode, null)
    }

    protected suspend fun processNode(node: TestNode, parent: TestNode.Group?): ResultNode =
        when (node) {
            is TestNode.Group -> {
                listener.groupStart(node)
                val result = executeGroup(node, parent)
                listener.groupFinish(result)
                result
            }
            is TestNode.Leaf -> {
                listener.leafStart(node)
                val result = executeLeaf(node, parent!!)
                listener.leafFinish(result)
                result
            }
        }

    abstract suspend fun executeLeaf(node: TestNode.Leaf, parent: TestNode.Group): ResultNode.Leaf
    abstract suspend fun executeGroup(node: TestNode.Group, parent: TestNode.Group?): ResultNode.Group


    interface Listener : TestingProcess.Listener {
        fun leafStart(node: TestNode.Leaf)
        fun leafFinish(node: ResultNode.Leaf)
        fun groupStart(node: TestNode.Group)
        fun groupFinish(node: ResultNode.Group)
    }
}