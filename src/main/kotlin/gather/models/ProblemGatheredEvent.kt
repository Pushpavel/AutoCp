package gather.models

import com.github.pushpavel.autocp.database.Problem

data class ProblemGatheredEvent(
    val problem: Problem,
    val gathered: Int,
    val total: Int,
)