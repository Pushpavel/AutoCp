package gather.models

import com.google.gson.JsonObject
import database.models.Problem

/**
 * Data class representing the Json Scheme of the data received
 * from Competitive Companion Browser extension
 */
data class ProblemJson(
    val name: String,
    val group: String,
    val url: String,
    val interactive: Boolean,
    val memoryLimit: Long,
    val timeLimit: Long,
    val tests: ArrayList<TestJson>,
    val testType: String,
    val input: JsonObject,
    val output: JsonObject,
    val languages: JsonObject,
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

