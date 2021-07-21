package config.ui

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import common.diff.DiffAdapter
import kotlinx.coroutines.CoroutineScope
import settings.langSettings.model.BuildConfig
import ui.vvm.View
import ui.vvm.swingModels.bindSelectionIndex
import ui.vvm.swingModels.plainDocument
import ui.vvm.swingModels.toCollectionComboBoxModel
import java.awt.BorderLayout
import java.nio.file.Path

class ConfigView(private val project: Project, private val parentDisposable: Disposable) :
    JBPanel<ConfigView>(BorderLayout()),
    View<ConfigViewModel> {
    private val solutionFileField = ExtendableTextField()
    private val configComboBox = ComboBox<BuildConfig>()

    init {
        // add macro support
        MacrosDialog.addTextFieldExtension(solutionFileField)

        // browse button for executable field
        solutionFileField.addBrowseButton()

        configComboBox.renderer = BuildConfig.cellRenderer()

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
        solutionFileField.document = plainDocument(viewModel.solutionFilePath)

        configComboBox.model = viewModel.buildConfigs.toCollectionComboBoxModel(this,
            object : DiffAdapter<BuildConfig> {
                override fun isSame(item1: BuildConfig, item2: BuildConfig) = item1.id == item2.id
            }
        )

        configComboBox.bindSelectionIndex(this, viewModel.selectedBuildConfigIndex)
    }

    private fun ExtendableTextField.addBrowseButton() {
        // ensures user can select only one file
        val solutionFileDescriptor = FileChooserDescriptorFactory
            .createSingleFileDescriptor()

        this.addBrowseExtension({
            val preselectPath = Path.of(this.text.ifEmpty { project.basePath })
            val selectedFile = VfsUtil.findFile(preselectPath, true)
            FileChooser.chooseFile(solutionFileDescriptor, project, selectedFile) {
                this.text = it.path
            }
        }, parentDisposable)
    }
}