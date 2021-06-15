package database.models

import dev.pushpavel.autocp.database.ProblemInfo
import dev.pushpavel.autocp.database.ProblemState
import dev.pushpavel.autocp.database.Testcase

data class ProblemSpec(
    val info: ProblemInfo,
    val state: ProblemState,
    val testcases: List<Testcase>
)