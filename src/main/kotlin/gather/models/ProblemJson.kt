package gather.models

import com.intellij.util.containers.OrderedSet
import dev.pushpavel.autocp.database.Problem

data class ProblemJson(
    val name: String,
    val group: String,
    val url: String,
    val memoryLimit: Int,
    val timeLimit: Int,
    val tests: ArrayList<TestJson>,
    val batch: BatchJson
) {

    fun toProblem(): Problem {

        val testcases = tests.mapIndexed { index, testJson ->
            testJson.toTestcase("Testcase #$index")
        }

        return Problem(name, group, OrderedSet(testcases), -1)
    }

}

