package com.github.pushpavel.autocp.gather.models

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.database.models.Testcase
import kotlinx.serialization.Serializable

/**
 * Data class representing the Json Scheme of the data received
 * from Competitive Companion Browser extension
 */
@Serializable
data class ProblemJson(
    val name: String,
    val group: String,
    val url: String,
    val memoryLimit: Long,
    val timeLimit: Long,
    val tests: ArrayList<TestJson>,
    val batch: BatchJson
) {

    fun toProblem(): Problem {

        // naming testcases
        val testcases = tests.mapIndexed { index, testJson ->
            testJson.toTestcase("Sample Testcase #${index + 1}")
        }

        return Problem(name, group, url, testcases, memoryLimit, timeLimit)
    }

}

/**
 * data type of a property in ProblemJson
 */
@Serializable
data class BatchJson(
    val id: String,
    val size: Int
)

/**
 * Json Schema of a Testcase in ProblemJson
 */
@Serializable
data class TestJson(
    val input: String,
    val output: String,
) {
    fun toTestcase(name: String) = Testcase(name, input, output)
}