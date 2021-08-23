package settings.langSettings.ui.dialogs

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import common.helpers.UniqueNameEnforcer
import common.res.R
import common.ui.dsl.withValidation
import settings.langSettings.model.BuildConfig

class BuildConfigDialog(
    private val buildConfig: BuildConfig,
    private val nameEnforcer: UniqueNameEnforcer,
    create: Boolean
) : DialogWrapper(false) {
    var name = buildConfig.name

    @Suppress("MemberVisibilityCanBePrivate")
    var buildCommand = buildConfig.commandTemplate
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
            row("Name") {
                textField(::name, 12)
                    .withValidation { validateName(it.text) }
            }

            row(R.strings.buildCommandLabel) {
                expandableTextField(::buildCommand)
                    .constraints(CCFlags.growX)
                    .comment(R.strings.buildCommandComment).component
            }

            row(R.strings.executeCommandLabel) {
                expandableTextField(::executeCommand)
                    .constraints(CCFlags.growX)
                    .comment(R.strings.executeCommandComment)
                    .withValidationOnInput {
                        if (it.text.isBlank())
                            error("Must not be empty")
                        else
                            null
                    }
            }
            commentRow(R.strings.commandTemplateDesc)
        }
    }.also { it.reset() }

    fun showAndGetConfig(): BuildConfig? {
        val confirm = showAndGet()

        return if (confirm)
            BuildConfig(buildConfig.id, name, buildCommand, executeCommand)
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
}