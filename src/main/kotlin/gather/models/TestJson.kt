package gather.models

import database.models.Testcase

data class TestJson(
    val input: String,
    val output: String,
) {
    fun toTestcase(name: String) = Testcase(name, input, output)
}