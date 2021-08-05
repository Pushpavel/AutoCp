package database

import com.github.pushpavel.autocp.database.Problem
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.containers.OrderedSet
import database.models.Testcase

/**
 * Interface for Persistence Layer of the plugin
 */
@Deprecated("use AutoCpDB")
interface AutoCpDatabase : AutoCloseable {
    fun insertProblems(problems: List<Problem>): Result<Unit>
    fun associateSolutionToProblem(solutionPath: String, problem: Problem): Result<Unit>
    fun getProblem(solutionPath: String): Result<Problem?>
    fun updateProblemState(problem: Problem, selectedIndex: Long): Result<Unit>
    fun updateTestcases(problem: Problem, testcases: OrderedSet<Testcase>): Result<Unit>
}

@Deprecated("use Project.autoCp()")
fun Project.autoCpDatabase(): AcpDatabase = service()