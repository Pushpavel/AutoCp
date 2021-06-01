package common

data class AutoCpProblem(
    val name: String,
    val group: String?,
    val batchId: String,
    val batchLength: Int,
    val tests: List<TestCase>,
) {
    constructor(data: ProblemJson) : this(
        data.name,
        data.group,
        data.batch.id,
        data.batch.size,
        data.tests.map { TestCase(it) }
    )
}

data class TestCase(
    val input: String,
    val output: String
) {
    constructor(data: TestJson) : this(
        data.input,
        data.output
    )
}

