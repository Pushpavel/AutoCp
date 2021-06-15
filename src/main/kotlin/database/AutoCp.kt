package database

import database.models.OldProblemSpec
import database.models.ProblemInfo
import database.models.ProblemState
import database.models.TestcaseSpec
import org.jetbrains.exposed.sql.Database

@Deprecated("interface lacks proper return types for functions")
interface AutoCp {

    @Deprecated("this is not necessary")
    val instance: Database

    // ProblemData
    fun addProblemData(spec: OldProblemSpec)
    fun getProblemData(solutionPath: String): OldProblemSpec?

    fun associateSolutionWithProblem(solutionPath: String, problemInfo: ProblemInfo)

    fun addTestcaseSpec(spec: TestcaseSpec)

    fun updateTestcaseSpecs(specs: List<TestcaseSpec>)
    fun updateProblemState(state: ProblemState)

}
