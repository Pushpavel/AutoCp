package database

import database.models.ProblemSpec
import database.models.ProblemInfo
import database.models.ProblemState
import database.models.TestcaseSpec
import org.jetbrains.exposed.sql.Database

interface AutoCp {

    @Deprecated("this is not necessary")
    val instance: Database

    // ProblemData
    fun addProblemData(spec: ProblemSpec)
    fun getProblemData(solutionPath: String): ProblemSpec?

    fun associateSolutionWithProblem(solutionPath: String, problemInfo: ProblemInfo)

    fun addTestcaseSpec(spec: TestcaseSpec)

    fun updateTestcaseSpecs(specs: List<TestcaseSpec>)
    fun updateProblemState(state: ProblemState)

}
