package tests

import database.AutoCp
import database.models.ProblemData
import database.models.ProblemSpec
import database.models.ProblemState
import database.models.TestcaseSpec
import database.utils.encodedJoin
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

abstract class AutoCpTest {

    private lateinit var database: AutoCp
    private lateinit var problemData: ProblemData
    private val problemId = encodedJoin("super", "groupName")

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        database = getInstance(tempDir)
        problemData = ProblemData(
            ProblemSpec("super", "groupName"),
            ProblemState(-1),
            listOf(TestcaseSpec("Testcase #1"))
        )
    }

    @Test
    fun basicSetupOperations() {
        database.addProblemData(problemData)
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionWithProblem(problemData.spec, solutionPath)
        val data = database.getProblemData(solutionPath)
        assertNotNull(data)
        assertEquals(problemData.spec, data!!.spec)
        assertEquals(problemData.state.selectedIndex, data.state.selectedIndex)
        assertEquals(problemData.testcases[0].name, data.testcases[0].name)
    }

    @Nested
    inner class MutationOperations {
        lateinit var solutionPath: String

        @BeforeEach
        fun setUp() {
            database.addProblemData(problemData)
            solutionPath = "C:\\path\\to\\solution.cpp"
            database.associateSolutionWithProblem(problemData.spec, solutionPath)
        }

        @Test
        fun updateProblemState() {
            database.updateProblemState(ProblemState(problemId, 34))
            val data = database.getProblemData(solutionPath)!!
            assertEquals(34, data.state.selectedIndex)
        }

        @Test
        fun updateTestcaseSpecs() {
            val data = database.getProblemData(solutionPath)!!
            val dataUpdate = TestcaseSpec(data.testcases[0].id, "ChangedName")
            database.updateTestcaseSpecs(listOf(dataUpdate))
            val changedData = database.getProblemData(solutionPath)
            assertNotNull(changedData)
            assertEquals(changedData!!.testcases, listOf(dataUpdate))
        }
    }

    abstract fun getInstance(tempDir: Path): AutoCp
}