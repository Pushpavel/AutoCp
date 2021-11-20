package com.github.pushpavel.autocp.core.execution

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.pathString
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.pathString

data class BuildOutput(val executeCommand: String, val dir: File, val output: ProcessOutput?)

sealed class BuildSolutionErr : Exception() {
    class InvalidPath(val solutionPathString: String) : BuildSolutionErr()
    class NoLang(val solutionPathString: String, val extension: String) : BuildSolutionErr()
    class BuildFailure(val solutionPathString: String, val output: ProcessOutput) : BuildSolutionErr()
}

fun buildSolutionExecutable(
    project: Project,
    solutionPathString: String,
    buildProgressIndicator: ProgressIndicator? = null
): BuildOutput {
    // validate solutionPathString
    if (!Path(solutionPathString).exists())
        throw BuildSolutionErr.InvalidPath(solutionPathString)

    // validate if lang exists
    val path = Path(solutionPathString)
    val extension = path.extension
    val lang = LangSettings.instance.langs[extension] ?: throw BuildSolutionErr.NoLang(solutionPathString, extension)

    val tempDir = Files.createTempDirectory("AutoCp").toFile()

    val executeCommand = lang.constructExecuteCommand(project, path.pathString, tempDir.path.pathString)

    val processOutput = if (lang.buildCommand == null) null else {
        val buildCommand = lang.constructBuildCommand(project, path.pathString, tempDir.path.pathString)
        ExecutionUtil.execAndGetOutput(buildCommand, tempDir, progressIndicator = buildProgressIndicator)
    }

    if ((processOutput?.exitCode ?: 1) != 0)
        throw BuildSolutionErr.BuildFailure(solutionPathString, processOutput!!)

    return BuildOutput(executeCommand, tempDir, processOutput)
}