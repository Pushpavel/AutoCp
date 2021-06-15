package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import database.models.ProblemSpec
import dev.pushpavel.autocp.database.*


@Service
class AcpDatabase(project: Project) : IAutoCp {

    private val db: AutoCpDatabase
    private val infoQ: ProblemInfoQueries
    private val stateQ: ProblemStateQueries
    private val testQ: TestcaseQueries
    private val relateQ: SolutionProblemQueries
    private val driver = JdbcSqliteDriver("${IN_MEMORY}${project.basePath}/.autocp")

    init {
        db = AutoCpDatabase.invoke(driver)

        infoQ = db.problemInfoQueries
        stateQ = db.problemStateQueries
        testQ = db.testcaseQueries
        relateQ = db.solutionProblemQueries
    }

    override fun insertProblemSpecs(specs: List<ProblemSpec>): Result<Unit> {
        try {
            db.transaction {
                for (spec in specs) {
                    infoQ.insertProblemInfo(spec.info)
                    stateQ.insertProblemState(spec.state)
                    for (testcase in spec.testcases)
                        testQ.insertTestcase(testcase)
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    override fun associateSolutionToProblem(solutionPath: String, info: ProblemInfo): Result<Unit> {
        try {
            db.transaction {
                relateQ.associateSolutionToProblem(
                    solutionPath,
                    info.name,
                    info.problemGroup
                )
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    override fun getProblemSpec(solutionPath: String): Result<ProblemSpec> {
        return try {
            val spec = db.transactionWithResult<ProblemSpec> {
                val relation = relateQ.getProblemSolution(solutionPath).executeAsOne()
                val info = infoQ.getProblemInfo(relation.problemName, relation.problemGroup).executeAsOne()
                val state = stateQ.getProblemState(relation.problemName, relation.problemGroup).executeAsOne()
                val testcases = db.testcaseQueries.getTestcases(relation.problemName, relation.problemGroup)
                    .executeAsList()

                ProblemSpec(info, state, testcases)
            }
            return Result.success(spec)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun close() = driver.getConnection().close()
}