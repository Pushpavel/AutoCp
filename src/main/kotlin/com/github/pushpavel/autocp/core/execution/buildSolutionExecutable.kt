package com.github.pushpavel.autocp.core.execution

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.project.Project
import groovy.lang.Tuple
import java.nio.file.Files

fun buildSolutionExecutable(project: Project, solutionFile: SolutionFile, lang: Lang): Tuple<Any?> {
    val tempDir = Files.createTempDirectory("AutoCp").toFile()

    val executeCommand = lang.constructExecuteCommand(project, solutionFile.pathString, tempDir.path.pathString)

    val buildOutput = if (lang.buildCommand == null) null else {
        val buildCommand = lang.constructBuildCommand(project, solutionFile.pathString, tempDir.path.pathString)
        ExecutionUtil.execAndGetOutput(buildCommand, tempDir)
    }
    return Tuple(executeCommand, tempDir, buildOutput)
}