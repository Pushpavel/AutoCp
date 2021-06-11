package tests

import com.intellij.openapi.project.Project
import database.AutoCp
import database.AutoCpDB
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Path
import kotlin.io.path.pathString

class AutoCpManagerTest : AutoCpTest() {
    override fun getInstance(tempDir: Path): AutoCp {
        val projectMock = mockk<Project>()
        every { projectMock.basePath } returns tempDir.pathString
        return AutoCpDB(projectMock)
    }
}