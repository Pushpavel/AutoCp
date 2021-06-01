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
        data.tests.mapIndexed { index, it -> TestCase(it, index) }
    )
}

data class TestCase(
    val input: String,
    val output: String,
    val index: Int,
) {
    constructor(data: TestJson, index: Int) : this(
        data.input,
        data.output,
        index
    )

    fun getName() = "Test Case #$index"
}

