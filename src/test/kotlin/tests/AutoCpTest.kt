package tests

import database.AutoCp
import database.models.OldProblemSpec
import database.models.ProblemInfo
import database.models.ProblemState
import database.models.TestcaseSpec
import database.utils.encodedJoin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

abstract class AutoCpTest {

    private lateinit var database: AutoCp
    private lateinit var problemSpec: OldProblemSpec
    private val problemId = encodedJoin("super", "groupName")

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        database = getInstance(tempDir)
        problemSpec = OldProblemSpec(
            ProblemInfo("super", "groupName"),
            ProblemState(-1),
            listOf(TestcaseSpec("Testcase #1", "Input", "Output"))
        )
    }

    @Test
    fun basicSetupOperations() {
        database.addProblemData(problemSpec)
        val solutionPath = "C:\\path\\to\\solution.cpp"
        database.associateSolutionWithProblem(solutionPath, problemSpec.info)
        val data = database.getProblemData(solutionPath)
        assertNotNull(data)
        assertEquals(problemSpec.info, data!!.info)
        assertEquals(problemSpec.state.selectedIndex, data.state.selectedIndex)

        // comparing testcases ignoring id
        problemSpec.testcases.zip(data.testcases).forEach { (first, second) ->
            val firstLike = first.copy(id = second.id)
            assertEquals(firstLike, second)
        }
    }

    @Nested
    inner class MutationOperations {
        lateinit var solutionPath: String

        @BeforeEach
        fun setUp() {
            database.addProblemData(problemSpec)
            solutionPath = "C:\\path\\to\\solution.cpp"
            database.associateSolutionWithProblem(solutionPath, problemSpec.info)
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
            val dataUpdate = problemSpec.testcases[0].copy(id = data.testcases[0].id, name = "ChangedName")
            database.updateTestcaseSpecs(listOf(dataUpdate))
            val changedData = database.getProblemData(solutionPath)
            assertNotNull(changedData)

            // comparing testcases ignoring id and changedName
            problemSpec.testcases.zip(changedData!!.testcases).forEach { (first, second) ->
                val firstLike = first.copy(id = second.id, name = "ChangedName")
                assertEquals(firstLike, second)
            }
        }
    }


    abstract fun getInstance(tempDir: Path): AutoCp
}