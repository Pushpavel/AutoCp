package tests

import com.intellij.openapi.project.Project
import database.AcpDatabase
import database.AutoCpDatabase
import io.mockk.every
import io.mockk.mockk

class AcpDatabaseTest : AutoCpDatabaseTest() {
    override fun getInstance(): AutoCpDatabase {
        val projectMock = mockk<Project>()
        every { projectMock.basePath } returns null
        return AcpDatabase(projectMock)
    }
}