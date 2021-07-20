package config.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import common.diff.DiffAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import settings.langSettings.model.BuildConfig
import ui.vvm.View
import ui.vvm.swingModels.toCollectionComboBoxModel
import ui.vvm.swingModels.toPlainDocument
import java.awt.BorderLayout

class ConfigView : JBPanel<ConfigView>(BorderLayout()), View<ConfigViewModel> {
    private val solutionFileField = ExtendableTextField()
    private val configComboBox = ComboBox<BuildConfig>()

    init {
        add(panel {
            row("Solution File:") {
                solutionFileField().constraints(CCFlags.growX)
            }
            row("Build Configuration:") {
                configComboBox()
            }
        }, BorderLayout.CENTER)
    }

    override fun CoroutineScope.onViewModelBind(viewModel: ConfigViewModel) {
        solutionFileField.document = viewModel.solutionFilePath.toPlainDocument(this)
        configComboBox.model = viewModel.buildConfigs.toCollectionComboBoxModel(this,
            object : DiffAdapter<BuildConfig> {
                override fun isSame(item1: BuildConfig, item2: BuildConfig) = item1.id == item2.id
            }
        )

        configComboBox.addActionListener {
            launch {
                viewModel.selectedBuildConfigId.emit(configComboBox.selectedItem as String?)
            }
        }

    }
}