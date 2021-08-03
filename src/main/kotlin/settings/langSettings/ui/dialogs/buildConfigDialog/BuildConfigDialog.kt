package settings.langSettings.ui.dialogs.buildConfigDialog

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.panel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import ui.ErrorView
import ui.helpers.mainScope
import ui.vvm.swingModels.plainDocument

class BuildConfigDialog(private val buildConfig: BuildConfig, list: List<BuildConfig>) : DialogWrapper(false) {

    private val scope = mainScope()
    private val model = BuildConfigViewModel(scope, buildConfig, list)

    init {
        title = "Edit ${buildConfig.name}"
        init()

        scope.launch {
            model.isValid.collect {
                isOKActionEnabled = it
            }
        }
    }

    override fun createCenterPanel() = panel {
        row {
            val nameField = ExtendableTextField(10).apply {
                document = scope.plainDocument(model.name)
            }
            val buildCommandField = ExtendableTextField(50).apply {
                document = scope.plainDocument(model.buildCommand)
            }
            MacrosDialog.addTextFieldExtension(buildCommandField)

            row("Name:") { nameField() }
            row("Build Command:") {
                buildCommandField(pushX).comment(
                    "This command will be executed to build the executable of your solution code.<br>" +
                            "@input@ will be replaced with \"path/to/input/file\" without quotes<br>" +
                            "@output@ will be replaced with \"path/to/output/file\" without quotes"
                )
            }
            val nameErrView = ErrorView(scope, model.nameErrors)
            val buildCommandErrView = ErrorView(scope, model.buildCommandErrors)

            row { nameErrView() }
            row { buildCommandErrView() }
        }
    }

    fun showAndGetConfig(): BuildConfig? {
        val confirm = showAndGet()

        return if (confirm)
            BuildConfig(buildConfig.id, model.name.value, model.buildCommand.value)
        else
            null
    }

    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}