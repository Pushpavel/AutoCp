package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.containers.OrderedSet
import database.diff.TestcaseDiff
import database.models.ProblemSpec
import dev.pushpavel.autocp.database.Problem
import dev.pushpavel.autocp.database.ProblemInfo
import dev.pushpavel.autocp.database.ProblemState
import dev.pushpavel.autocp.database.Testcase


@Service
class AcpDatabase(project: Project) : AbstractAcpDatabase(project) {

    private val testcaseDiff = TestcaseDiff(testQ)

    override fun insertProblemSpecs(specs: List<ProblemSpec>) = runCatching {
        db.transaction {
            for (spec in specs) {
                infoQ.insertProblemInfo(spec.info)
                stateQ.insertProblemState(spec.state)
                for (testcase in spec.testcases)
                    testQ.insertTestcase(testcase)
            }
        }
    }

    override fun associateSolutionToProblem(solutionPath: String, info: ProblemInfo) = runCatching {
        relateQ.associateSolutionToProblem(
            solutionPath,
            info.name,
            info.problemGroup
        )
    }


    override fun getProblemSpec(solutionPath: String) = runCatching {
        val relation = relateQ.getProblemSolution(solutionPath).executeAsOne()
        val info = infoQ.getProblemInfo(relation.problemName, relation.problemGroup).executeAsOne()
        val state = stateQ.getProblemState(relation.problemName, relation.problemGroup).executeAsOne()
        val testcases = db.testcaseQueries.getTestcases(relation.problemName, relation.problemGroup)
            .executeAsList()

        return@runCatching ProblemSpec(info, state, testcases)
    }

    override fun updateProblemState(state: ProblemState) = runCatching {
        stateQ.updateProblemState(state)
    }


    override fun updateTestcases(info: ProblemInfo, testcases: List<Testcase>) = runCatching {
        val prevTestcases = testQ.getTestcases(info.name, info.problemGroup).executeAsList()
        testcaseDiff.applyUpdates(prevTestcases, testcases)
    }


    override fun updateTestcase(testcase: Testcase) = runCatching {
        testQ.updateTestcase(testcase)
    }

    override fun addTestcase(testcase: Testcase) = runCatching {
        testQ.insertTestcase(testcase)
    }

    override fun removeTestcase(testcase: Testcase) = runCatching {
        testQ.removeTestcase(testcase.name, testcase.problemName, testcase.problemGroup)
    }

    override fun associateSolutionToProblem(solutionPath: String, problem: Problem): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateProblemState(selectedIndex: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateTestcases(problem: Problem, testcases: OrderedSet<Unit>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun insertProblems(problems: List<Problem>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getProblem(solutionPath: String): Result<Problem> {
        TODO("Not yet implemented")
    }
}