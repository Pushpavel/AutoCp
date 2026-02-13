package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.DefaultFileGenerator
import com.github.pushpavel.autocp.gather.models.FileGenerationDto
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import java.nio.file.InvalidPathException
import javax.swing.JCheckBox
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.pathString

var extension = ""
var rootDir = ""
var dontAskBeforeFileGeneration = false
var template: String? = ""
var saveRootDir = true
var problemNames = mutableListOf<String>()

fun showProblemGatheringDialog(project: Project, problems: List<Problem>): List<FileGenerationDto>? {
    val projectSettings = project.autoCpProject()
    val generalSettings = AutoCpGeneralSettings.instance

    extension = projectSettings.defaultFileExtension
    dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration
    template = problems.firstOrNull()?.getOnlineJudge()
    rootDir = projectSettings.fileGenerationRoot.getOrDefault(template, generalSettings.fileGenerationRoot)
    problemNames = MutableList(problems.size) { problems[it].getDefaultName(project) }

    val dialog = object : DialogWrapper(project, false) {
        init {
            title = "Generate Files (${problems.firstOrNull()?.groupName})"
            isOKActionEnabled = extension.isNotBlank()
            init()
        }

        override fun createCenterPanel() = panel() {
            lateinit var saveRootCheckbox: Cell<JCheckBox>
            lateinit var rootDirInput: Cell<JBTextField>
            row {}.comment(R.strings.problemGatheringDialogMsg)
            row("Template name") {
                val comboBox = comboBox(projectSettings.fileGenerationRoot.keys)
                    .columns(20)
                    .applyToComponent {
                        isEditable = true
                        selectedItem = template
                    }

                comboBox.onReset {
                    comboBox.component.selectedItem = template
                }.onIsModified {
                    template != problems.firstOrNull()?.getOnlineJudge()
                }.onApply {
                    template = (comboBox.component.editor.item as? String)?.trim().orEmpty()
                }.validationOnInput {
                    template = (comboBox.component.editor.item as? String)?.trim().orEmpty()
                    saveRootCheckbox.component.text = ("Save file generation root as default for $template")
                    if (projectSettings.fileGenerationRoot.containsKey(template)) {
                        rootDir = projectSettings.fileGenerationRoot[template]!!
                        rootDirInput.component.text = rootDir
                    }
                    null
                }
            }
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
            row("File generation root") {
                rootDirInput = textField().bindText(::rootDir)
                    .onReset { rootDir = projectSettings.fileGenerationRoot.getOrDefault(template, generalSettings.fileGenerationRoot) }
                    .onIsModified { rootDir != projectSettings.fileGenerationRoot.get(template) }
                    .onApply {
                        if (!saveRootDir)
                            return@onApply
                        try {
                            projectSettings.fileGenerationRoot[template?: ""] = if (rootDir.isNotBlank())
                                Path(rootDir).pathString
                            else ""
                        } catch (e: InvalidPathException) {
                            // ignored
                        }
                    }
                    .validationOnInput{
                        try {
                            val p = Path(it.text)
                            when {
                                p.isAbsolute -> error("Should not be an absolute path")
                                p.extension != "" -> error("Should correspond to a directory")
                                else -> null
                            }
                        } catch (e: InvalidPathException) {
                            error(e.localizedMessage)
                        }
                    }
                    .comment(R.strings.fileGenerationRootComment)
            }

            row {
                saveRootCheckbox = checkBox("Save file generation root as default for $template").bindSelected(::saveRootDir)
            }

            row {
                checkBox("Don't ask again").bindSelected(::dontAskBeforeFileGeneration)
                    .onReset { dontAskBeforeFileGeneration = !projectSettings.askBeforeFileGeneration }
                    .onIsModified { dontAskBeforeFileGeneration == projectSettings.askBeforeFileGeneration }
                    .onApply { projectSettings.askBeforeFileGeneration = !dontAskBeforeFileGeneration }
            }
            problems.forEachIndexed() { index, problem ->
                row(problem.name) {
                    textField().bindText({ problemNames[index] }, { problemNames[index] = it })
                        .onIsModified { problemNames[index] != problem.getDefaultName(project) }
                }
            }
        }
    }

    val confirmed = dialog.showAndGet()
    if (!confirmed)
        return null;

    return List(problemNames.size) { FileGenerationDto(problemNames[it], rootDir, template) }
}

fun Problem.getDefaultName(project: Project) = DefaultFileGenerator(project).getValidFileName(name)

fun Problem.getOnlineJudge() = groupName.split("-")[0].trim();
