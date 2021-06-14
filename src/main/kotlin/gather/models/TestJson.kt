package gather.models

import database.models.TestcaseSpec

data class TestJson(
    val input: String,
    val output: String,
) {
    fun toTestcaseSpec(name: String) = TestcaseSpec(name, input, output)
}