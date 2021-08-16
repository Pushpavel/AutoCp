package tester.tree

import tester.errors.Verdict


/**
 * Tree Data-structure containing result of the Test Tree
 *
 * Model classes containing Results after execution of a [TestNode]
 */
sealed interface ResultNode {
    /**
     * [TestNode] from which this [ResultNode] is created
     */
    val sourceNode: TestNode

    /**
     * Contains results of a testcase execution
     */
    data class Leaf(
        override val sourceNode: TestNode.Leaf,
        val verdict: Verdict
    ) : ResultNode

    /**
     * Contains results of group of nodes which is not necessarily [ResultNode.Leaf]
     */
    data class Group(
        override val sourceNode: TestNode.Group,
        val children: List<ResultNode>
    ) : ResultNode
}