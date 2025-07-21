package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.common.ui.helpers.isError
import com.github.pushpavel.autocp.config.validators.SolutionFilePathErr
import com.github.pushpavel.autocp.config.validators.getValidSolutionFile
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.layout.ValidationInfoBuilder

/**
 * UI Editor of [AutoCpConfig] Run Configuration
 */
open class ConfigEditor(val project: Project, val config: AutoCpConfig) : SettingsEditor<AutoCpConfig>(), DumbAware {
    @Suppress("MemberVisibilityCanBePrivate")
    var solutionFilePath = ""

    private lateinit var editor: DialogPanel
    private val invalidFields = mutableMapOf<String, Boolean>()

    override fun createEditor() = panel {
        row("Solution File:") {
            textFieldWithBrowseButton("Select Solution File").bindText(::solutionFilePath)
                .validationInfo { validateSolutionFilePath(it.text) }
        }
    }.also {
        editor = it
    }


    private fun ValidationInfoBuilder.validateSolutionFilePath(pathString: String): ValidationInfo? {
        val info = try {
            getValidSolutionFile(project, config.name, pathString)
            null
        } catch (e: SolutionFilePathErr) {
            when (e) {
                is SolutionFilePathErr.BlankPathErr -> error("Must not be empty")
                is SolutionFilePathErr.FileDoesNotExist -> error("File does not exists")
                is SolutionFilePathErr.FileNotRegistered -> warning("AutoCp is not enabled for this file")
                is SolutionFilePathErr.FormatErr -> error("Invalid path")
            }
        } catch (e: Exception) {
            error("Unknown error")
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