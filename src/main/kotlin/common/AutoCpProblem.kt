package common

@Deprecated("use ProblemSpec")
data class AutoCpProblem(
    val name: String,
    val group: String?,
    val batchId: String,
    val batchLength: Int,
    val tests: List<TestCase>,
)

@Deprecated("use TestcaseSpec")
data class TestCase(
    val input: String,
    val output: String,
    val index: Int,
) {

    fun getName() = "Test Case #$index"
}

