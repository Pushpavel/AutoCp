package database.models

data class ProblemSpec(
    val info: ProblemInfo,
    val state: ProblemState,
    val testcases: List<TestcaseSpec>
)