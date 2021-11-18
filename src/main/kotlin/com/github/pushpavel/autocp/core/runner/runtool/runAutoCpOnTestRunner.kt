package com.github.pushpavel.autocp.core.runner.runtool

import com.github.pushpavel.autocp.build.settings.LangNotConfiguredErr
import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.config.AutoCpConfig
import com.github.pushpavel.autocp.config.validators.getValidSolutionFile
import com.github.pushpavel.autocp.core.execution.buildSolutionExecutable
import com.github.pushpavel.autocp.core.runner.judge.JudgingProcess
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.Path
import kotlin.io.path.extension

suspend fun ProcessLikeHandler.runAutoCpOnTestRunner(project: Project, config: AutoCpConfig) {
    // todo: create a separate class that handles notifying ProcessLikeHandler

    // validate path
    getValidSolutionFile(project, config.name, config.solutionFilePath)

    // validate if lang exists
    val extension = Path(config.solutionFilePath).extension
    val lang = LangSettings.instance.langs[extension] ?: throw LangNotConfiguredErr(extension)

    // build solution file
    val buildOutput = withContext(Dispatchers.IO) { buildSolutionExecutable(project, config.solutionFilePath, lang) }

    // execute local judge
    project.service<JudgingProcess>().execute(buildOutput, config.solutionFilePath)
}