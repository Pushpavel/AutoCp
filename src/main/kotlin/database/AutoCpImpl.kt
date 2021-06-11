package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import database.models.ProblemData
import database.models.ProblemSpec
import database.models.ProblemState
import database.models.TestcaseSpec
import database.tables.*
import database.utils.encodedJoin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalStateException

@Service
class AutoCpImpl(project: Project) : AbstractAutoCpImpl(project) {


    override fun addProblemData(data: ProblemData) {
        transaction(instance) {
            val id = addProblemSpecAndState(data.spec, data.state)
            addTestcase(id, data.testcases)
        }
    }

    override fun getProblemData(solutionPath: String): ProblemData? {
        return transaction(instance) {
            val id = getProblemIdOfSolution(solutionPath) ?: return@transaction null
            val pair = getProblemSpecAndState(id)
                ?: throw IllegalStateException("Problem Associated with $solutionPath is probably deleted")
            val testcases = getTestcases(id)
            return@transaction ProblemData(pair.first, pair.second, testcases)
        }
    }

    override fun associateSolutionWithProblem(problemSpec: ProblemSpec, solutionPath: String) {
        transaction(instance) {
            SolutionSpecs.insert {
                it[this.solutionPath] = solutionPath
                it[this.problemId] = encodedJoin(problemSpec.name, problemSpec.group)
            }
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