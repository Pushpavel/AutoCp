package database.models

import dev.pushpavel.autocp.database.ProblemInfo

@Deprecated("use problemInfo in dev.pushpavel.autocp.database")
data class ProblemInfo(
    val name: String,
    val group: String
) {

    constructor(problem: ProblemInfo) : this(problem.name, problem.problemGroup)

}