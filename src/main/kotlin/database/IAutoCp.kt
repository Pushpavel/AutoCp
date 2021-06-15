package database

import database.models.ProblemSpec
import dev.pushpavel.autocp.database.ProblemInfo

interface IAutoCp : AutoCloseable {
    fun insertProblemSpecs(specs: List<ProblemSpec>): Result<Unit>
    fun associateSolutionToProblem(solutionPath: String, info: ProblemInfo): Result<Unit>
    fun getProblemSpec(solutionPath: String): Result<ProblemSpec>
}