package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import database.models.ProblemSpec
import dev.pushpavel.autocp.database.*
import java.nio.file.Paths
import kotlin.io.path.pathString


@Service
class AcpDatabase(project: Project) : IAutoCp {
    private val dbPath = project.basePath?.let { Paths.get(it, ".autocp").pathString } ?: ""
    private val db: AutoCpDatabase
    private val infoQ: ProblemInfoQueries
    private val stateQ: ProblemStateQueries
    private val testQ: TestcaseQueries
    private val relateQ: SolutionProblemQueries
    private val driver = JdbcSqliteDriver(IN_MEMORY + dbPath)

    init {
        val version = getVersion()

        if (version == 0) {
            AutoCpDatabase.Schema.create(driver)
            setVersion(1)
        } else {
            val schemaVer = AutoCpDatabase.Schema.version
            if (schemaVer > version) {
                AutoCpDatabase.Schema.migrate(driver, version, schemaVer)
                setVersion(schemaVer)
            }
        }
        db = AutoCpDatabase(driver)

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

    private fun getVersion(): Int {
        val sqlCursor = driver.executeQuery(null, "PRAGMA user_version;", 0, null)
        return sqlCursor.getLong(0)!!.toInt()
    }

    private fun setVersion(version: Int) {
        driver.execute(null, String.format("PRAGMA user_version = %d;", version), 0, null)
    }

}