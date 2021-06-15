package tests

import com.intellij.openapi.project.Project
import database.AcpDatabase
import database.IAutoCp
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

class AcpDatabaseTest : IAutoCpTest() {
    override fun getInstance(): IAutoCp {
        val projectMock = mockk<Project>()
        every { projectMock.basePath } returns null
        return AcpDatabase(projectMock)
    }
}