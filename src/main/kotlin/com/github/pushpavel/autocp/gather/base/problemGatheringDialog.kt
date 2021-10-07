package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel

var extension = ""
var dontAskBeforeFileGeneration = false

fun showProblemGatheringDialog(project: Project, groupName: String): Boolean {
    val projectSettings = project.autoCpProject()
    extension = projectSettings.defaultFileExtension
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