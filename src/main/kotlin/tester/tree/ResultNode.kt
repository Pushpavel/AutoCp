package tester.tree

import tester.models.ResultCode


sealed interface ResultNode {
    val sourceNode: TestNode

    data class Leaf(
        override val sourceNode: TestNode.Leaf,
        val verdict: ResultCode,
        val verdictError: String,
        val output: String,
        val error: String,
        val executionTime: Long
    ) : ResultNode

    data class Group(
        override val sourceNode: TestNode.Group,
        val children: List<ResultNode>
    ) : ResultNode
}