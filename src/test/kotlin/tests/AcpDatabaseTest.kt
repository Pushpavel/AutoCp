package tests

import com.intellij.openapi.project.Project
import database.AcpDatabase
import database.IAutoCp
import dev.pushpavel.autocp.database.AutoCpDatabase
import io.mockk.every
import io.mockk.mockk
import org.junit.Ignore
import java.nio.file.Path
import kotlin.io.path.pathString

class AcpDatabaseTest : IAutoCpTest() {
    override fun getInstance(tempDir: Path): IAutoCp {
        val projectMock = mockk<Project>()
        every { projectMock.basePath } returns tempDir.pathString
        return AcpDatabase(projectMock)
    }
}