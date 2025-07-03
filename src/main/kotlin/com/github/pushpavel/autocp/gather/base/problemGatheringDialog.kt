package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.DefaultFileGenerator
import com.github.pushpavel.autocp.gather.models.FileGenerationDto
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.generalSettings.FileGenerationRootRow
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel

var extension = ""
var rootDir = ""
var dontAskBeforeFileGeneration = false
var problemNames = mutableListOf<String>()

fun showProblemGatheringDialog(project: Project, problems: List<Problem>): List<FileGenerationDto>? {
    val projectSettings = project.autoCpProject()
    val generalSettings = AutoCpGeneralSettings.instance

    extension = projectSettings.defaultFileExtension
    rootDir = generalSettings.fileGenerationRoot
    dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration
    problemNames = MutableList(problems.size) { problems[it].getDefaultName() }

    val dialog = object : DialogWrapper(project, false) {
        init {
            title = "Generate Files (${problems.firstOrNull()?.groupName})"
            isOKActionEnabled = extension.isNotBlank()
            init()
        }

        override fun createCenterPanel() = panel() {
            row {}.comment(R.strings.problemGatheringDialogMsg)
            row("File Extension") {
                textField().bindText(::extension).columns(2)
                    .focused()
                    .onReset { extension = projectSettings.defaultFileExtension }
                    .onIsModified { extension != projectSettings.defaultFileExtension }
                    .onApply {
                        if (extension.isNotBlank())
                            projectSettings.defaultFileExtension = extension.trim().replace(".", "")
                    }.validationOnInput {
                        if (it.text.isNotBlank())
                            null
                        else
                            error("Should not be empty")
                    }
            }
            FileGenerationRootRow().placeUI(this)

            row {
                checkBox("Don't ask again").bindSelected(::dontAskBeforeFileGeneration)
                    .onReset { dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration }
                    .onIsModified { dontAskBeforeFileGeneration == projectSettings.askBeforeFileGeneration }
                    .onApply { projectSettings.askBeforeFileGeneration = !dontAskBeforeFileGeneration }
            }
            problems.forEachIndexed() { index, problem ->
                row(problem.name) {
                    textField().bindText({ problemNames[index] }, { problemNames[index] = it })
                        .onIsModified { problemNames[index] != problem.getDefaultName() }
                }
            }
        }
    }

    val confirmed = dialog.showAndGet()
    if (!confirmed)
        return null;

    return List(problemNames.size) { FileGenerationDto(problemNames[it], generalSettings.fileGenerationRoot) }
}

fun Problem.getDefaultName() = DefaultFileGenerator.defaultConversion(name)