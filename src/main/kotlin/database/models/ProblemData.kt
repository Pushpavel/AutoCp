package database.models

data class ProblemData(
    val spec: ProblemSpec,
    val state: ProblemState,
    val testcases: List<TestcaseSpec>
)