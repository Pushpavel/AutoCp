package com.github.pushpavel.autocp.core.execution

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.project.Project
import java.io.File
import java.nio.file.Files

data class BuildOutput(val executeCommand: String, val dir: File, val output: ProcessOutput?)

fun buildSolutionExecutable(project: Project, solutionFile: SolutionFile, lang: Lang): BuildOutput {
    val tempDir = Files.createTempDirectory("AutoCp").toFile()

    val executeCommand = lang.constructExecuteCommand(project, solutionFile.pathString, tempDir.path.pathString)

    val buildOutput = if (lang.buildCommand == null) null else {
        val buildCommand = lang.constructBuildCommand(project, solutionFile.pathString, tempDir.path.pathString)
        ExecutionUtil.execAndGetOutput(buildCommand, tempDir)
    }
    return BuildOutput(executeCommand, tempDir, buildOutput)
}