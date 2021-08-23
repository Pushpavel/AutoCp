package settings.langSettings.ui.dialogs

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import common.helpers.UniqueNameEnforcer
import common.ui.dsl.withValidation
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.model.BuildConfig

class BuildConfigDialog(
    private val buildConfig: BuildConfig,
    private val nameEnforcer: UniqueNameEnforcer,
    create: Boolean
) : DialogWrapper(false) {
    var name = buildConfig.name

    @Suppress("MemberVisibilityCanBePrivate")
    var commandTemplate = buildConfig.commandTemplate
    var executeCommand = buildConfig.executeCommand

    init {
        title = if (create)
            "Create New Build Configuration"
        else
            "Edit ${buildConfig.name}"

        init()
    }

    override fun createCenterPanel() = panel {
        row {
            row("Name:") {
                textField(::name, 12)
                    .withValidation { validateName(it.text) }
            }
            row("Command Template:") {
                expandableTextField(::commandTemplate)
                    .constraints(CCFlags.growX)
                    .applyToComponent { MacrosDialog.addTextFieldExtension(this) }
                    .withValidation { validateBuildCommand(it.text) }
                    .comment(
                        "AutoCp uses this template to construct a command that builds an executable " +
                                "on which your testcases are tested, " +
                                "@in in this template string would be replaced with path to solution file with quotes " +
                                "and @out will be replaced with output path for the executable." +
                                "@out can be ignored if executing this command does not produce any executable."
                    )
            }
        }
    }.also { it.reset() }

    fun showAndGetConfig(): BuildConfig? {
        val confirm = showAndGet()

        return if (confirm)
            BuildConfig(buildConfig.id, name, commandTemplate, executeCommand)
        else
            null
    }


    private fun ValidationInfoBuilder.validateName(name: String): ValidationInfo? {
        if (name.isBlank())
            return error("Must not be empty")

        if (name != buildConfig.name && nameEnforcer.buildUniqueName(name) != name)
            return error("\"$name\" already exists")

        return null
    }

    private fun ValidationInfoBuilder.validateBuildCommand(commandTemplate: String): ValidationInfo? {
        val input = commandTemplate.contains(AutoCpGeneralSettings.INPUT_PATH_KEY)

        val errorMessage = when {
            !input -> "${AutoCpGeneralSettings.INPUT_PATH_KEY} missing, This will be replaced with path to solution file ex- \"C:\\solution.cpp\""
            else -> null
        }

        return errorMessage?.let { error(it) }
    }
}