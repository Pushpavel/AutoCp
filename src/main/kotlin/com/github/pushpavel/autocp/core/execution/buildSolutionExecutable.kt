package com.github.pushpavel.autocp.core.execution

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.pathString
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.pathString

data class BuildOutput(
    val lang: Lang,
    val buildCommand: String?,
    val executeCommand: String,
    val dir: File,
    val output: ExecutionUtil.Output?
)

sealed class BuildSolutionErr : Exception() {
    class InvalidPath(val solutionPathString: String) : BuildSolutionErr()
    class NoLang(val solutionPathString: String, val extension: String) : BuildSolutionErr()
    class BuildFailure(val buildOutput: BuildOutput, val output: ExecutionUtil.Output) : BuildSolutionErr()
}

fun prepareSolutionExecutable(project: Project, solutionPathString: String): BuildOutput {
    // validate solutionPathString
    if (!Path(solutionPathString).exists())
        throw BuildSolutionErr.InvalidPath(solutionPathString)

    // validate if lang exists
    val path = Path(solutionPathString)
    val extension = path.extension
    val lang = LangSettings.instance.langs[extension] ?: throw BuildSolutionErr.NoLang(solutionPathString, extension)

    val tempDir = Files.createTempDirectory("AutoCp").toFile()

    val executeCommand = lang.constructExecuteCommand(project, path.pathString, tempDir.path.pathString)
    val buildCommand = if (lang.buildCommand == null) null else lang.constructBuildCommand(
        project,
        path.pathString,
        tempDir.path.pathString
    )

    return BuildOutput(lang, buildCommand, executeCommand, tempDir, null)
}

fun buildSolutionExecutable(buildOutput: BuildOutput, buildProgressIndicator: ProgressIndicator?): BuildOutput {
    if (buildOutput.buildCommand == null) return buildOutput

    val executionOutput = ExecutionUtil.execAndGetOutput(
        buildOutput.buildCommand,
        buildOutput.dir,
        progressIndicator = buildProgressIndicator
    )


    if (executionOutput.processOutput.exitCode != 0)
        throw BuildSolutionErr.BuildFailure(buildOutput, executionOutput)

    return buildOutput.copy(output = executionOutput)
}