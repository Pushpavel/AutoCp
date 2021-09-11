package com.github.pushpavel.autocp.config

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.common.ui.dsl.comboBoxView
import com.github.pushpavel.autocp.common.ui.dsl.startValidating
import com.github.pushpavel.autocp.common.ui.dsl.withValidation
import com.github.pushpavel.autocp.common.ui.helpers.isError
import com.github.pushpavel.autocp.database.autoCp
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.BuildConfig
import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import kotlin.io.path.Path

/**
 * UI Editor of [AutoCpConfig] Run Configuration
 */
class ConfigEditor(project: Project) : SettingsEditor<AutoCpConfig>(), DumbAware {
    @Suppress("MemberVisibilityCanBePrivate")
    var solutionFilePath = ""
    private var buildConfigId: String? = null

    private val buildConfigsModel = CollectionComboBoxModel<BuildConfig>()

    private var lang: Lang? = null

    private lateinit var editor: DialogPanel
    private val db = project.autoCp()
    private val invalidFields = mutableMapOf<String, Boolean>()

    override fun createEditor() = panel {
        row("Solution File:") {
            textFieldWithBrowseButton(::solutionFilePath, "Select Solution File")
                .withValidation { validateSolutionFilePath(it.text) }
        }
        row("Build Configuration:") {
            comboBoxView(
                buildConfigsModel,
                { it.id == buildConfigId },
                { buildConfigId = it?.id },
                BuildConfig.cellRenderer()
            ).withValidation {
                val info = if (lang != null && buildConfigsModel.isEmpty) {
                    error("No Build Configuration is setup for ${lang?.getLanguage()?.displayName}")
                } else null

                invalidFields["buildConfigId"] = info.isError()

                info
            }
        }
    }.also {
        editor = it
        it.startValidating(this)
    }


    private fun ValidationInfoBuilder.validateSolutionFilePath(pathString: String): ValidationInfo? {
        // TODO: replace this validation with [config.validators.getValidSolutionFile]
        val info = run {
            if (pathString.isBlank())
                return@run error("Must not be empty")

            val file: VirtualFile?

            try {
                file = LocalFileSystem.getInstance().findFileByNioFile(Path(pathString))
            } catch (e: Exception) {
                return@run error("Invalid path")
            }

            if (file?.exists() != true)
                return@run error("File does not exists")

            lang = AutoCpLangSettings.findLangByFile(file)
                ?: return@run warning("File's language is not registered in Settings/Preferences > Tools > AutoCp > Languages")

            buildConfigsModel.replaceAll(lang!!.buildConfigs.values.toList())

            if (!db.solutionFiles.containsKey(pathString))
                return@run warning("AutoCp is not enabled for this file")

            null
        }

        if (info.isError()) {
            lang = null
            buildConfigsModel.removeAll()
        }

        invalidFields["solutionFilePath"] = info.isError()

        return info
    }

    /**
     * Settings to UI
     */
    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFilePath = s.solutionFilePath.pathString
        buildConfigId = s.buildConfigId
        editor.reset()
    }

    /**
     * UI to Settings
     */
    override fun applyEditorTo(s: AutoCpConfig) {
        editor.apply()

        if (invalidFields.any { it.value })
            return

        s.solutionFilePath = solutionFilePath.pathString
        s.buildConfigId = buildConfigId
    }

}