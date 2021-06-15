package database

import database.models.ProblemSpec
import dev.pushpavel.autocp.database.ProblemInfo
import dev.pushpavel.autocp.database.ProblemState
import dev.pushpavel.autocp.database.Testcase

interface IAutoCp : AutoCloseable {
    fun insertProblemSpecs(specs: List<ProblemSpec>): Result<Unit>
    fun associateSolutionToProblem(solutionPath: String, info: ProblemInfo): Result<Unit>
    fun getProblemSpec(solutionPath: String): Result<ProblemSpec>
    fun updateProblemState(state: ProblemState): Result<Unit>
    fun updateTestcases(info: ProblemInfo, testcases: List<Testcase>): Result<Unit>
    fun updateTestcase(testcase: Testcase): Result<Unit>
    fun addTestcase(testcase: Testcase): Result<Unit>
    fun removeTestcase(testcase: Testcase): Result<Unit>
}