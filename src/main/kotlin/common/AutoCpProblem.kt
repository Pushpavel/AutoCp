package common

data class AutoCpProblem(
    val name: String,
    val group: String?,
    val tests: ArrayList<TestCase>
)

data class TestCase(
    val input: String,
    val output: String
)

