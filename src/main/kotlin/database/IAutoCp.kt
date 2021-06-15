package database

import com.intellij.util.containers.OrderedSet
import database.models.ProblemSpec
import dev.pushpavel.autocp.database.Problem
import dev.pushpavel.autocp.database.ProblemInfo
import dev.pushpavel.autocp.database.ProblemState
import dev.pushpavel.autocp.database.Testcase

interface IAutoCp : AutoCloseable {
    @Deprecated("use insertProblems")
    fun insertProblemSpecs(specs: List<ProblemSpec>): Result<Unit>

    @Deprecated("use associateSolutionToProblem with Problem as arg")
    fun associateSolutionToProblem(solutionPath: String, info: ProblemInfo): Result<Unit>

    @Deprecated("use getProblem")
    fun getProblemSpec(solutionPath: String): Result<ProblemSpec>

    @Deprecated("use updateProblemState with selectedIndex as arg")
    fun updateProblemState(state: ProblemState): Result<Unit>

    @Deprecated("use updateProblemState with selectedIndex as arg")
    fun updateTestcases(info: ProblemInfo, testcases: List<Testcase>): Result<Unit>

    @Deprecated("use updateTestcases")
    fun updateTestcase(testcase: Testcase): Result<Unit>

    @Deprecated("use updateTestcases")
    fun addTestcase(testcase: Testcase): Result<Unit>

    @Deprecated("use updateTestcases")
    fun removeTestcase(testcase: Testcase): Result<Unit>

    fun insertProblems(problems: List<Problem>): Result<Unit>
    fun associateSolutionToProblem(solutionPath: String, problem: Problem): Result<Unit>
    fun getProblem(solutionPath: String): Result<Problem>
    fun updateProblemState(selectedIndex: Int): Result<Unit>
    fun updateTestcases(problem: Problem, testcases: OrderedSet<Unit>): Result<Unit>
}