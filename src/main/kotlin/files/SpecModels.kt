package files

import java.io.File

data class ProblemSpec(
    var name: String,
    val solutionFiles: List<String>,
    val testcases: List<TestcaseSpec>,
) {

    @Transient
    lateinit var file: File
}

data class TestcaseSpec(
    val index: Int,
    val input: String,
    val output: String,
) {
    fun getName(): String {
        return "Testcase #$index"
    }
}