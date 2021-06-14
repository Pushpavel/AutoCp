package gather.models

import database.models.ProblemInfo

data class ProblemJson(
    val name: String,
    val group: String,
    val url: String,
    val memoryLimit: Int,
    val timeLimit: Int,
    val tests: ArrayList<TestJson>,
    val batch: BatchJson
) {

    fun toGatheredProblem(): GatheredProblem {
        val spec = ProblemInfo(name, group)
        val testcases = tests.mapIndexed { index, testJson ->
            testJson.toTestcaseSpec("Testcase #$index")
        }

        return GatheredProblem(spec, testcases)
    }

}

