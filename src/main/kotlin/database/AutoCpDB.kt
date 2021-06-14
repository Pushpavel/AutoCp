package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import database.models.ProblemSpec
import database.models.ProblemInfo
import database.models.ProblemState
import database.models.TestcaseSpec
import database.tables.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalStateException

@Service
@Deprecated("use AcpDatabase")
class AutoCpDB(project: Project) : AbstractAutoCpDB(project) {


    override fun addProblemData(spec: ProblemSpec) {
        transaction(instance) {
            val id = addProblemSpecAndState(spec.info, spec.state)
            addTestcase(id, spec.testcases)
        }
    }

    override fun getProblemData(solutionPath: String): ProblemSpec? {
        return transaction(instance) {
            val id = SolutionSpecs.withSolutionPath(solutionPath) ?: return@transaction null
            val pair = getProblemSpecAndState(id)
                ?: throw IllegalStateException("Problem Associated with $solutionPath is probably deleted")
            val testcases = TestcaseSpecs.withProblemId(id)
            return@transaction ProblemSpec(pair.first, pair.second, testcases)
        }
    }

    override fun associateSolutionWithProblem(solutionPath: String, problemInfo: ProblemInfo) {
        transaction(instance) {
            SolutionSpecs.insertModel(problemInfo, solutionPath)
        }
    }

    override fun addTestcaseSpec(spec: TestcaseSpec) {
        TODO("Not yet implemented")
    }

    override fun updateTestcaseSpecs(specs: List<TestcaseSpec>) {
        transaction(instance) {
            specs.forEach { spec ->
                TestcaseSpecs.update({ TestcaseSpecs.id eq spec.id }) {
                    it[name] = spec.name
                }
            }
        }
    }

    override fun updateProblemState(state: ProblemState) {
        transaction(instance) {
            ProblemSpecs.update({ ProblemSpecs.problemId eq state.problemId }) {
                it[this.selectedIndex] = state.selectedIndex
            }
        }
    }

}