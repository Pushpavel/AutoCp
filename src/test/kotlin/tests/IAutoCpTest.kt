package tests

import database.IAutoCp
import database.models.ProblemSpec
import database.utils.encodedJoin
import dev.pushpavel.autocp.database.ProblemInfo
import dev.pushpavel.autocp.database.ProblemState
import dev.pushpavel.autocp.database.Testcase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

abstract class IAutoCpTest {

    private lateinit var database: IAutoCp
    private lateinit var problemSpec: ProblemSpec

    @BeforeEach
    fun setUp() {
        database = getInstance()
    }

    @AfterEach
    fun teardown() = database.close()

    @Test
    fun basicSetupOperations() {

        problemSpec = ProblemSpec(
            ProblemInfo("name", "group"),
            ProblemState(-1, "name", "group"),
            listOf(
                Testcase("Testcase #1", "Input", "Output", "name", "group"),
            )
        )

        database.insertProblemSpecs(listOf(problemSpec))
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionToProblem(solutionPath, problemSpec.info)
        val data = database.getProblemSpec(solutionPath).getOrThrow()
        assertNotNull(data)
        assertEquals(problemSpec.info, data.info)
        assertEquals(problemSpec.state.selectedIndex, data.state.selectedIndex)

        // comparing testcases ignoring id
        assertArrayEquals(problemSpec.testcases.toTypedArray(), data.testcases.toTypedArray())
    }

    @Test
    fun updateProblemState() {
        problemSpec = ProblemSpec(
            ProblemInfo("name", "group"),
            ProblemState(-1, "name", "group"),
            listOf()
        )

        database.insertProblemSpecs(listOf(problemSpec))
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionToProblem(solutionPath, problemSpec.info)

        val updatedProblemState = problemSpec.state.copy(selectedIndex = 34)
        database.updateProblemState(updatedProblemState)
        val data = database.getProblemSpec(solutionPath).getOrThrow()
        assertEquals(updatedProblemState, data.state)
    }


    @Nested
    inner class UpdateTestcaseSpecs {
        private lateinit var solutionPath: String
        private lateinit var solutionPath2: String
        private lateinit var problemSpec2: ProblemSpec

        @BeforeEach
        fun setUp() {
            problemSpec = ProblemSpec(
                ProblemInfo("name", "group"),
                ProblemState(-1, "name", "group"),
                listOf(
                    Testcase("Testcase #1", "Input 1", "Output 1", "name", "group"),
                    Testcase("Testcase #2", "Input 2", "Output 2", "name", "group"),
                    Testcase("Testcase #3", "Input 3", "Output 3", "name", "group"),
                )
            )
            // exactly equal to problemSpec except problemName
            problemSpec2 = ProblemSpec(
                ProblemInfo("name2", "group"),
                ProblemState(-1, "name2", "group"),
                listOf(
                    Testcase("Testcase #1", "Input 1", "Output 1", "name2", "group"),
                    Testcase("Testcase #2", "Input 2", "Output 2", "name2", "group"),
                )
            )

            database.insertProblemSpecs(listOf(problemSpec, problemSpec2))
            solutionPath = "C:\\path\\to\\solution.cpp"
            solutionPath2 = "C:\\path\\to\\second\\solution.cpp"
            database.associateSolutionToProblem(solutionPath, problemSpec.info)
            database.associateSolutionToProblem(solutionPath2, problemSpec2.info)
        }

        @Test
        fun `non key updates`() {
            val data = database.getProblemSpec(solutionPath).getOrThrow()

            // changing only outputs of testcases
            val updatedTestcases = data.testcases.mapIndexed { index, it ->
                it.copy(output = "output changed $index")
            }

            database.updateTestcases(problemSpec.info, updatedTestcases)

            checkTestcases(updatedTestcases)
        }

        @Test
        fun `key updates should remove and add the testcase`() {
            val data = database.getProblemSpec(solutionPath).getOrThrow()

            val updatedTestcases = data.testcases.toMutableList()
            updatedTestcases[1] = updatedTestcases[1].copy(name = "Test case nameChange")

            database.updateTestcases(problemSpec.info, updatedTestcases)

            checkTestcases(updatedTestcases)
        }


        @Test
        fun `adding and removing testcases`() {
            val data = database.getProblemSpec(solutionPath).getOrThrow()

            val updatedTestcases = data.testcases.toMutableList()
            updatedTestcases.removeAt(0)
            updatedTestcases.add(
                Testcase("New Testcase", "new input", "new output", "name", "group")
            )
            checkTestcases(updatedTestcases)
        }

        private fun checkTestcases(testcases: List<Testcase>) {
            val changedData = database.getProblemSpec(solutionPath).getOrThrow()
            assertNotNull(changedData)

            assertEquals(testcases, changedData.testcases)
        }
    }


    abstract fun getInstance(): IAutoCp
}