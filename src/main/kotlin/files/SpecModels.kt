package files

import gather.models.ProblemJson
import gather.models.TestJson
import java.io.File

@Deprecated("use database.models.ProblemSpec")
data class ProblemSpec(
    var name: String,
    val group: String,
    val solutionFiles: ArrayList<String>,
    var testcases: MutableList<TestcaseSpec>,
    var selectedIndex: Int?
) {

    @Transient
    @Deprecated("use specFilePath")
    lateinit var file: File

    @Transient
    var specFilePath: String? = null

    constructor(data: ProblemJson) : this(
        data.name,
        data.group,
        ArrayList(),
        data.tests.mapIndexed { index, it -> TestcaseSpec(index, it) } as ArrayList<TestcaseSpec>,
        null
    )
}

@Deprecated("use database.models.TestcaseSpec")
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