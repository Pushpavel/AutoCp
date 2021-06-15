package database

import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.pushpavel.autocp.database.*
import java.nio.file.Paths
import kotlin.io.path.pathString

abstract class AbstractAcpDatabase(project: Project) : IAutoCp {
    private val dbPath = project.basePath?.let { Paths.get(it, ".autocp").pathString } ?: ""
    protected val db: AutoCpDatabase

    @Deprecated("use ProblemQueries")
    protected val infoQ: ProblemInfoQueries

    @Deprecated("use ProblemQueries")
    protected val stateQ: ProblemStateQueries

    @Deprecated("use ProblemQueries")
    protected val testQ: TestcaseQueries

    protected val relateQ: SolutionProblemQueries
    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + dbPath)

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

    override fun close() = driver.getConnection().close()

    private fun getVersion(): Int {
        val sqlCursor = driver.executeQuery(null, "PRAGMA user_version;", 0, null)
        return sqlCursor.getLong(0)!!.toInt()
    }

    private fun setVersion(version: Int) {
        driver.execute(null, String.format("PRAGMA user_version = %d;", version), 0, null)
    }
}