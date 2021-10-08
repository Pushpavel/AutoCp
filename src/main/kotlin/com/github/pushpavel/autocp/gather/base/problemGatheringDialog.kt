package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import java.nio.file.InvalidPathException
import kotlin.io.path.Path
import kotlin.io.path.pathString

var extension = ""
var rootDir = ""
var dontAskBeforeFileGeneration = false

fun showProblemGatheringDialog(project: Project, groupName: String): Boolean {
    val projectSettings = project.autoCpProject()
    val generalSettings = AutoCpGeneralSettings.instance

    extension = projectSettings.defaultFileExtension
    rootDir = generalSettings.fileGenerationRoot
    dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration

    val dialog = object : DialogWrapper(project, false) {
        init {
            title = "Generate Files (${groupName})"
            isOKActionEnabled = extension.isNotBlank()
            init()
        }

        override fun createCenterPanel() = panel(LCFlags.fill) {
            row {}.comment(R.strings.problemGatheringDialogMsg)
            row("File Extension") {
                textField(::extension, 2)
                    .focused()
                    .onReset { extension = projectSettings.defaultFileExtension }
                    .onIsModified { extension != projectSettings.defaultFileExtension }
                    .onApply {
                        if (extension.isNotBlank())
                            projectSettings.defaultFileExtension = extension.trim().replace(".", "")
                    }.withValidationOnInput {
                        if (it.text.isNotBlank())
                            null
                        else
                            error("Should not be empty")
                    }
            }
            row("File Generation Root") {
                textField(::rootDir)
                    .onReset { rootDir = generalSettings.fileGenerationRoot }
                    .onIsModified { rootDir != generalSettings.fileGenerationRoot }
                    .onApply {
                        try {
                            generalSettings.fileGenerationRoot = if (rootDir.isNotBlank())
                                Path(rootDir).pathString
                            else ""
                        } catch (e: InvalidPathException) {
                            // ignored
                        }
                    }.withValidationOnInput {
                        try {
                            if (it.text.isNotBlank())
                                Path(it.text)
                            null
                        } catch (e: InvalidPathException) {
                            error(e.localizedMessage)
                        }
                    }.comment(R.strings.fileGenerationRootComment)
            }
            row {
                checkBox("Don't ask again", ::dontAskBeforeFileGeneration)
                    .onReset { dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration }
                    .onIsModified { dontAskBeforeFileGeneration == projectSettings.askBeforeFileGeneration }
                    .onApply { projectSettings.askBeforeFileGeneration = !dontAskBeforeFileGeneration }
            }
        }
    }

    return dialog.showAndGet()
}