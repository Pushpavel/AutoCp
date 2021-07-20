package config

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import config.ui.ConfigView
import config.ui.ConfigViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.swing.JComponent

/**
 * UI Editor of [AutoCpConfig] Run Configuration
 */
class ConfigEditor(private val project: Project) : SettingsEditor<AutoCpConfig>() {
    lateinit var scope: CoroutineScope
    lateinit var model: ConfigViewModel


    /**
     * Settings to UI
     */
    override fun resetEditorFrom(s: AutoCpConfig) {
        scope.launch {
            model.solutionFilePath.emit(s.solutionFilePath)

            // emit selectedBuildConfigIndex from buildConfigId
            val configId = s.buildConfigId.takeIf { it != -1L }
            val index = model.getIndexOfBuildConfigId(configId)
            model.selectedBuildConfigIndex.emit(index)
        }
    }

    /**
     * UI to Settings
     */
    override fun applyEditorTo(s: AutoCpConfig) {
        s.solutionFilePath = model.solutionFilePath.value
        scope.launch {
            val index = model.selectedBuildConfigIndex.value
            val config = model.getBuildConfigOfIndex(index)
            if (config != null)
                s.buildConfigId = config.id
        }
    }

    override fun createEditor(): JComponent {
        scope = MainScope()
        model = ConfigViewModel("", null)
        return ConfigView(project).apply {
            bindToViewModel(scope, model)
        }
    }

    override fun disposeEditor() {
        super.disposeEditor()
        scope.cancel()
    }
}