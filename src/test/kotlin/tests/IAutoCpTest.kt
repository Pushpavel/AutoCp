package tests

import com.intellij.util.containers.OrderedSet
import database.IAutoCp
import database.models.Testcase
import dev.pushpavel.autocp.database.Problem
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class IAutoCpTest {

    private lateinit var database: IAutoCp
    private lateinit var problem: Problem

    @BeforeEach
    fun setUp() {
        database = getInstance()
    }

    @AfterEach
    fun teardown() = database.close()

    @Test
    fun basicSetupOperations() {
        problem = Problem(
            "name",
            "group",
            OrderedSet(listOf(Testcase("Testcase #1", "Input", "Output"))),
            -1
        )

        database.insertProblems(listOf(problem))
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionToProblem(solutionPath, problem)
        val data = database.getProblem(solutionPath).getOrThrow()
        assertNotNull(data)
        assertEquals(problem, data)

        // comparing testcases ignoring id
        assertArrayEquals(problem.testcases.toTypedArray(), data.testcases.toTypedArray())
    }

    @Test
    fun updateProblemState() {
        problem = Problem("name", "group", OrderedSet(), -1)

        database.insertProblems(listOf(problem))
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionToProblem(solutionPath, problem)
        database.updateProblemState(problem, 34)
        val data = database.getProblem(solutionPath).getOrThrow()

        assertEquals(problem.copy(selectedTestcaseIndex = 34), data)
    }

    @Test
    fun updateTestcases() {
        problem = Problem(
            "name",
            "group",
            OrderedSet(
                listOf(
                    Testcase("Testcase #1", "Input 1", "Output 1"),
                    Testcase("Testcase #2", "Input 2", "Output 2"),
                    Testcase("Testcase #3", "Input 3", "Output 3"),
                )
            ),
            -1
        )
        val problem2 = problem.copy(name = "name2")

        // initializing database
        database.insertProblems(listOf(problem, problem2))
        val solutionPath = "C:\\path\\to\\solution.cpp"
        val solutionPath2 = "C:\\path\\to\\second\\solution.cpp"
        database.associateSolutionToProblem(solutionPath, problem)
        database.associateSolutionToProblem(solutionPath2, problem2)

        // removed 3rd testcase & changed 1st test case
        val updatedTestcases = OrderedSet(
            listOf(
                Testcase("Testcase #Changed", "Input Changed", "Output 1"),
                Testcase("Testcase #2", "Input 2", "Output 2"),
            )
        )

        database.updateTestcases(problem, updatedTestcases)

        val data = database.getProblem(solutionPath).getOrThrow()

        assertEquals(updatedTestcases, data.testcases)
    }


    abstract fun getInstance(): IAutoCp
}