package com.github.pushpavel.autocp.tester.tree

import com.github.pushpavel.autocp.database.models.JudgeSettings
import com.github.pushpavel.autocp.database.models.Testcase
import com.github.pushpavel.autocp.tester.base.Judge
import com.github.pushpavel.autocp.tester.base.ProcessRunner

/**
 * Tree Data-structure for defining the Test Tree
 *
 * Model classes containing specifications for execution of [TreeTestingProcess]
 */
sealed interface TestNode {
    val name: String

    data class Leaf(
        override val name: String,
        val testcase: Testcase,
    ) : TestNode

    data class Group(
        override val name: String,
        val children: Sequence<TestNode>,
        val settings: JudgeSettings,
        val haltOnFailing: Boolean,
        val participant: ProcessRunner,
        val judge: Judge?
    ) : TestNode
}
