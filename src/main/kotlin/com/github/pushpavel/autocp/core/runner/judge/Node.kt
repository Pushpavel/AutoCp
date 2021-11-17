package com.github.pushpavel.autocp.core.runner.judge

import com.github.pushpavel.autocp.core.execution.BuildOutput
import com.intellij.execution.process.ProcessOutput

data class TestNode(
    val buildOutput: BuildOutput,
    val input: String,
    val expectedOutput: String,
    val timeLimit: Int
)

data class ResultNode(
    val source: TestNode,
    val output: ProcessOutput,
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
