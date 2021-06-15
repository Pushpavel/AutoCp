package tests

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.pushpavel.autocp.database.AutoCpDatabase
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

class Playground {

    @Test
    @Disabled("there is a bug in 3rd party library")
    fun play(@TempDir dir: Path) {
        val dbFilePath = Paths.get(dir.pathString, "data.db")
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + dbFilePath.pathString)
        AutoCpDatabase(driver)
        AutoCpDatabase.Schema.create(driver)
        driver.getConnection().close() // this does not close the file (bug)
        Files.delete(dbFilePath)
    }

}