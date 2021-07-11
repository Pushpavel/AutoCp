package tester.tree

import tester.judge.Verdict

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
        val verdict: Verdict,
        val verdictError: String,
        val output: String,
        val error: String,
        val executionTime: Long
    ) : ResultNode

    /**
     * Contains results of group of nodes which is not necessarily [ResultNode.Leaf]
     */
    data class Group(
        override val sourceNode: TestNode.Group,
        val children: List<ResultNode>
    ) : ResultNode
}