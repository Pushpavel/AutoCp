package settings.langSettings.ui.dialogs

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import common.helpers.UniqueNameEnforcer
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.model.BuildConfig

class BuildConfigDialog(
    private val buildConfig: BuildConfig,
    private val nameEnforcer: UniqueNameEnforcer,
    create: Boolean
) : DialogWrapper(false) {
    var name = buildConfig.name
    var buildCommand = buildConfig.buildCommand

    init {
        title = if (create)
            "Create New Build Configuration"
        else
            "Edit ${buildConfig.name}"

        isOKActionEnabled = false
        init()
    }

    override fun createCenterPanel() = panel {
        row {
            row("Name:") {
                textField(::name, 12)
                    .withValidationOnInput { validateName(it.text) }
            }
            row("Build Command:") {
                expandableTextField(::buildCommand)
                    .constraints(CCFlags.growX)
                    .applyToComponent { MacrosDialog.addTextFieldExtension(this) }
                    .withValidationOnInput { validateBuildCommand(it.text) }
            }
        }
    }.also { it.reset() }

    fun showAndGetConfig(): BuildConfig? {
        val confirm = showAndGet()

        return if (confirm)
            BuildConfig(buildConfig.id, name, buildCommand)
        else
            null
    }


    private fun ValidationInfoBuilder.validateName(name: String): ValidationInfo? {
        if (name.isBlank())
            return error("Must not be empty")

        if (nameEnforcer.buildUniqueName(name) != name)
            return error("\"$name\" already exists")

        return null
    }

    private fun ValidationInfoBuilder.validateBuildCommand(buildCommand: String): ValidationInfo? {
        val input = buildCommand.contains(AutoCpGeneralSettings.INPUT_PATH_KEY)
        val output = buildCommand.contains(AutoCpGeneralSettings.OUTPUT_PATH_KEY)

        // TODO: check if single quotes are useful so that we can just hardcode double quotes directly on the path
        val inputDoubleQuotes = buildCommand.contains("\"" + AutoCpGeneralSettings.INPUT_PATH_KEY + "\"")
        val inputSingleQuotes = buildCommand.contains("'" + AutoCpGeneralSettings.INPUT_PATH_KEY + "'")
        val outputDoubleQuotes = buildCommand.contains("\"" + AutoCpGeneralSettings.OUTPUT_PATH_KEY + "\"")
        val outputSingleQuotes = buildCommand.contains("'" + AutoCpGeneralSettings.OUTPUT_PATH_KEY + "'")

        val errorMessage = when {
            !input -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} missing"
            !output -> "buildCommand: ${AutoCpGeneralSettings.OUTPUT_PATH_KEY} missing"
            !inputSingleQuotes && !inputDoubleQuotes -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} should be wrapped with single or double quotes"
            !outputSingleQuotes && !outputDoubleQuotes -> "buildCommand: ${AutoCpGeneralSettings.INPUT_PATH_KEY} should be wrapped with single or double quotes"
            else -> null
        }

        return errorMessage?.let { error(it) }
    }
}