package tester.tree

import tester.base.SolutionProcessFactory

/**
 * Tree Data-structure for defining the Test Tree
 *
 * Model classes containing specifications for execution of [TreeTestingProcess]
 */
sealed interface TestNode {
    val name: String
    val id: String
    val processFactory: SolutionProcessFactory?

    data class Leaf(
        override val name: String,
        override val id: String,
        val input: String,
        val expectedOutput: String,
        override val processFactory: SolutionProcessFactory,
    ) : TestNode

    data class Group(
        override val name: String,
        override val id: String,
        val timeLimit: Long,
        val children: List<TestNode>,
        override val processFactory: SolutionProcessFactory?
    ) : TestNode
}
