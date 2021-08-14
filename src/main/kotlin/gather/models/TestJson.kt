package gather.models

import database.models.Testcase
import kotlinx.serialization.Serializable

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