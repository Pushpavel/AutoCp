package tester.tree

import tester.base.ProcessFactory

/**
 * Tree Data-structure for defining the Test Tree
 *
 * Model classes containing specifications for execution of [TreeTestingProcess]
 */
sealed interface TestNode {
    val name: String
    val processFactory: ProcessFactory?

    data class Leaf(
        override val name: String,
        val input: String,
        val expectedOutput: String,
        override val processFactory: ProcessFactory,
    ) : TestNode

    data class Group(
        override val name: String,
        val timeLimit: Long,
        val children: List<TestNode>,
        override val processFactory: ProcessFactory?
    ) : TestNode
}
