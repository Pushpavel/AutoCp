package tester.tree

/**
 * Tree Data-structure for defining the Test Tree
 */
sealed interface TestNode {
    val name: String
    val id: String

    data class Leaf(
        override val name: String,
        override val id: String,
        val input: String,
        val expectedOutput: String,
    ) : TestNode

    data class Group(
        override val name: String,
        override val id: String,
        val children: List<TestNode>
    ) : TestNode
}
