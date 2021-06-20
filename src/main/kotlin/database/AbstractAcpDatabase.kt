package database

import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.utils.DataColumnAdapter
import database.utils.TestcaseColumnAdapter
import dev.pushpavel.autocp.database.*
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.pathString

abstract class AbstractAcpDatabase(project: Project) : IAutoCp {
    private val dbPath = project.basePath?.let { Paths.get(it, ".autocp").pathString } ?: ""

    protected val db: AutoCpDatabase

    protected val problemQ: ProblemQueries
    protected val relateQ: SolutionProblemQueries

    private val driver = JdbcSqliteDriver((JdbcSqliteDriver.IN_MEMORY + dbPath), Properties())

    init {
        // workaround for https://stackoverflow.com/questions/43529832/trying-to-connect-to-a-sqlite-database-keep-getting-no-suitable-driver-found
        Class.forName("org.sqlite.JDBC")

        db = AutoCpDatabase(driver, Problem.Adapter(TestcaseColumnAdapter(), DataColumnAdapter()))

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

        problemQ = db.problemQueries
        relateQ = db.solutionProblemQueries
    }

    override fun close() = driver.getConnection().close()

    private fun getVersion(): Int {
        return driver.executeQuery(null, "PRAGMA user_version;", 0, null).use { cursor ->
            if (cursor.next())
                cursor.getLong(0)?.toInt()
            else null
        } ?: 0
    }

    private fun setVersion(version: Int) {
        driver.execute(null, String.format("PRAGMA user_version = %d;", version), 0, null)
    }
}