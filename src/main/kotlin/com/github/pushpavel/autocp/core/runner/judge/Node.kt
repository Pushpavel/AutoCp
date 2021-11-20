package com.github.pushpavel.autocp.core.runner.judge

import com.github.pushpavel.autocp.core.execution.BuildOutput
import com.github.pushpavel.autocp.core.execution.ExecutionUtil

data class TestNode(
    val name: String,
    val buildOutput: BuildOutput,
    val input: String,
    val expectedOutput: String,
    val timeLimit: Int
)

data class ResultNode(
    val source: TestNode,
    val output: ExecutionUtil.Output,
    val verdict: Verdict
)

data class TestGroupNode(
    val name: String,
    val testNodes: List<TestNode>,
)

data class ResultGroupNode(
    val testGroupNode: TestGroupNode,
    val resultNodes: List<ResultNode>,
)

enum class Verdict {
    CORRECT_ANSWER,
    WRONG_ANSWER,
    TIME_LIMIT_EXCEEDED,
    CANCELLED,
    RUNTIME_ERROR
}
