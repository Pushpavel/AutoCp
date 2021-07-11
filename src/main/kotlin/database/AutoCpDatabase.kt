package database

import com.intellij.util.containers.OrderedSet
import database.models.Testcase
import com.github.pushpavel.autocp.database.Problem

/**
 * Interface for Persistence Layer of the plugin
 */
interface AutoCpDatabase : AutoCloseable {
    fun insertProblems(problems: List<Problem>): Result<Unit>
    fun associateSolutionToProblem(solutionPath: String, problem: Problem): Result<Unit>
    fun getProblem(solutionPath: String): Result<Problem?>
    fun updateProblemState(problem: Problem, selectedIndex: Long): Result<Unit>
    fun updateTestcases(problem: Problem, testcases: OrderedSet<Testcase>): Result<Unit>
}