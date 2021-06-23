package bugs

import com.github.pushpavel.autocp.database.AutoCpDatabaseTransactor
import com.github.pushpavel.autocp.database.Problem
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.adapters.DataColumnAdapter
import database.adapters.TestcaseColumnAdapter
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

class DatabaseFileIsNotClosedBug {

    @Test
    @Disabled("this bug is in a 3rd party library")
    fun solved(@TempDir dir: Path) {
        val dbFilePath = Paths.get(dir.pathString, "data.db")
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + dbFilePath.pathString)
        AutoCpDatabaseTransactor(driver, Problem.Adapter(TestcaseColumnAdapter(), DataColumnAdapter()))
        AutoCpDatabaseTransactor.Schema.create(driver)
        driver.getConnection().close() // this does not close the file (bug)
        Files.delete(dbFilePath)
    }
}