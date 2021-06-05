package files

import plugin.services.ProblemJson
import plugin.services.TestJson
import java.io.File

data class ProblemSpec(
    var name: String,
    val group: String,
    val solutionFiles: ArrayList<String>,
    val testcases: ArrayList<TestcaseSpec>,
) {

    @Transient
    lateinit var file: File

    constructor(data: ProblemJson) : this(
        data.name,
        data.group,
        ArrayList(),
        data.tests.mapIndexed { index, it -> TestcaseSpec(index, it) } as ArrayList<TestcaseSpec>,
    )
}

data class TestcaseSpec(
    val index: Int,
    val input: String,
    val output: String,
) {

    constructor(index: Int, data: TestJson) : this(index, data.input, data.output)

    fun getName(): String {
        return "Testcase #$index"
    }
}