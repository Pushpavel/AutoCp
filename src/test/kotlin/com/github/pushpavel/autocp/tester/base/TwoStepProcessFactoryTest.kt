package com.github.pushpavel.autocp.tester.base

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.github.pushpavel.autocp.tester.errors.ProcessRunnerErr
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mockito
import java.io.File
import java.nio.file.Path

class TwoStepProcessFactoryTest {

    @TempDir
    lateinit var tempDir: Path

    private val isWindows = System.getProperty("os.name").lowercase().contains("win")

    @Test
    fun `failed build with no output throws ProcessRunnerErr RuntimeErr with fallback message`() {
        val project = Mockito.mock(Project::class.java)
        val workingDir = tempDir.toFile()
        val solutionFilePath = File(workingDir, "solution.cpp").apply { createNewFile() }.absolutePath
        val solutionFile = SolutionFile(pathString = solutionFilePath, linkedProblemId = null, testcases = emptyList())

        val buildCommand = if (isWindows) "cmd /c exit 1" else "sh -c \"exit 1\""
        val lang = Lang("cpp", buildCommand, "echo test", true)

        val buildErr = assertThrows(BuildErr::class.java) {
            runBlocking {
                TwoStepProcessFactory.fromSolutionFile(project, solutionFile, lang, workingDir)
            }
        }

        val runtimeErr = assertInstanceOf(ProcessRunnerErr.RuntimeErr::class.java, buildErr.err)
        assertEquals("", runtimeErr.output)
        assertTrue(
            runtimeErr.message!!.contains("\nNo output was produced by the build command."),
            "Expected fallback message when build produces no output, but was: ${runtimeErr.message}"
        )
    }

    @Test
    fun `failed build with output throws ProcessRunnerErr RuntimeErr with compiler output`() {
        val project = Mockito.mock(Project::class.java)
        val workingDir = tempDir.toFile()
        val solutionFilePath = File(workingDir, "solution.cpp").apply { createNewFile() }.absolutePath
        val solutionFile = SolutionFile(pathString = solutionFilePath, linkedProblemId = null, testcases = emptyList())

        val expectedOutput = "fatal error C1083: Cannot open include file"
        val buildCommand = if (isWindows) {
            "cmd /c \"echo $expectedOutput & exit 1\""
        } else {
            "sh -c \"echo '$expectedOutput' && exit 1\""
        }
        val lang = Lang("cpp", buildCommand, "echo test", true)

        val buildErr = assertThrows(BuildErr::class.java) {
            runBlocking {
                TwoStepProcessFactory.fromSolutionFile(project, solutionFile, lang, workingDir)
            }
        }

        val runtimeErr = assertInstanceOf(ProcessRunnerErr.RuntimeErr::class.java, buildErr.err)
        assertEquals(expectedOutput, runtimeErr.output)
        assertTrue(
            runtimeErr.message!!.contains(expectedOutput),
            "Expected compiler output in message, but was: ${runtimeErr.message}"
        )
    }
}
