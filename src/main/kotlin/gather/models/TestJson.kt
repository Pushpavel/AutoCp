package gather.models

import database.models.Testcase

/**
 * Json Schema of a Testcase in ProblemJson
 */
data class TestJson(
    val input: String,
    val output: String,
) {
    fun toTestcase(name: String) = Testcase(name, input, output)
}