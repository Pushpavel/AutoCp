package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.pushpavel.autocp.database.AutoCpDatabase
import dev.pushpavel.autocp.database.ProblemQueries

@Service
class AcpDatabase(project: Project) {

    private val db: AutoCpDatabase
    private val problemQueries: ProblemQueries

    init {
        val driver = JdbcSqliteDriver("jdbc:sqlite:${project.basePath}/.autocp")
        db = AutoCpDatabase(driver)
        problemQueries = db.problemQueries
    }

}