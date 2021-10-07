package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.common.ui.dsl.startValidating
import com.github.pushpavel.autocp.common.ui.dsl.withValidation
import com.github.pushpavel.autocp.common.ui.helpers.isError
import com.github.pushpavel.autocp.database.SolutionFiles
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import kotlin.io.path.Path

/**
 * UI Editor of [AutoCpConfig] Run Configuration
 */
class ConfigEditor(project: Project) : SettingsEditor<AutoCpConfig>(), DumbAware {
    @Suppress("MemberVisibilityCanBePrivate")
    var solutionFilePath = ""

    private lateinit var editor: DialogPanel
    private val solutionFiles = SolutionFiles.getInstance(project)
    private val invalidFields = mutableMapOf<String, Boolean>()

    override fun createEditor() = panel {
        row("Solution File:") {
            textFieldWithBrowseButton(::solutionFilePath, "Select Solution File")
                .withValidation { validateSolutionFilePath(it.text) }
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

            if (pathString !in solutionFiles)
                return@run warning("AutoCp is not enabled for this file")

            null
        }

        invalidFields["solutionFilePath"] = info.isError()

        return info
    }

    /**
     * Settings to UI
     */
    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFilePath = s.solutionFilePath.pathString
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
    }

}