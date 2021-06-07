package files

import plugin.services.ProblemJson
import plugin.services.TestJson
import java.io.File

data class ProblemSpec(
    var name: String,
    val group: String,
    val solutionFiles: ArrayList<String>,
    var testcases: MutableList<TestcaseSpec>,
    var selectedIndex: Int?
) {

    @Transient
    lateinit var file: File

    constructor(data: ProblemJson) : this(
        data.name,
        data.group,
        ArrayList(),
        data.tests.mapIndexed { index, it -> TestcaseSpec(index, it) } as ArrayList<TestcaseSpec>,
        null
    )
}

data class TestcaseSpec(
    @Deprecated("do not store index here")
    val index: Int,
    val input: String,
    val output: String,
) {

    constructor(index: Int, data: TestJson) : this(index, data.input, data.output)

    fun getName(): String {
        return "Testcase #$index"
    }
}