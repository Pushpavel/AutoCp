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
    private val problemId = encodedJoin("super", "groupName")

    @BeforeEach
    fun setUp() {
        database = getInstance()
        val name = "super"
        val group = "groupName"

        problemSpec = ProblemSpec(
            ProblemInfo(name, group),
            ProblemState(-1, name, group),
            listOf(
                Testcase("Testcase #1", "Input", "Output", name, group),
            )
        )
    }

    @AfterEach
    fun teardown() = database.close()

    @Test
    fun basicSetupOperations() {
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

//    @Nested
//    inner class MutationOperations {
//        lateinit var solutionPath: String
//
//        @BeforeEach
//        fun setUp() {
//            database.insertProblemSpecs(listOf(problemSpec))
//            solutionPath = "C:\\path\\to\\solution.cpp"
//            database.associateSolutionToProblem(solutionPath, problemSpec.info)
//        }
//
//        @Test
//        fun updateProblemState() {
//            database.updateProblemState(ProblemState(problemId, 34))
//            val data = database.getProblemData(solutionPath)!!
//            assertEquals(34, data.state.selectedIndex)
//        }
//
//        @Test
//        fun updateTestcaseSpecs() {
//            val data = database.getProblemData(solutionPath)!!
//            val dataUpdate = problemSpec.testcases[0].copy(id = data.testcases[0].id, name = "ChangedName")
//            database.updateTestcaseSpecs(listOf(dataUpdate))
//            val changedData = database.getProblemData(solutionPath)
//            assertNotNull(changedData)
//
//            // comparing testcases ignoring id and changedName
//            problemSpec.testcases.zip(changedData!!.testcases).forEach { (first, second) ->
//                val firstLike = first.copy(id = second.id, name = "ChangedName")
//                assertEquals(firstLike, second)
//            }
//        }
//    }


    abstract fun getInstance(): IAutoCp
}