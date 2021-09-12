package com.github.pushpavel.autocp.config.validators

import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileTypes.FileTypeManager
import com.github.pushpavel.autocp.common.errors.NoReachErr
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.BuildConfig
import kotlin.io.path.Path
import kotlin.io.path.name

fun getValidBuildConfig(solutionFile: SolutionFile, buildConfigId: String?): BuildConfig {
    // check for buildConfig only in the language of the solutionFile
    val fileName = Path(solutionFile.pathString).name

    // get lang of the solutionFile
    val fileType = FileTypeManager.getInstance().getFileTypeByFileName(fileName)
    val language = LanguageUtil.getFileTypeLanguage(fileType)
    val lang = AutoCpLangSettings.instance.languages[language?.id] ?: throw NoReachErr

    return lang.getBuildConfig(buildConfigId) ?: throw NoBuildConfigErr(lang)
}