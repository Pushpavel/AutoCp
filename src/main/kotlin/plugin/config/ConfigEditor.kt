package plugin.config

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import org.jetbrains.annotations.NotNull
import settings.AutoCpSettings
import settings.SolutionLanguage
import java.nio.file.Path
import javax.swing.JComponent

class ConfigEditor(private val project: @NotNull Project) : SettingsEditor<AutoCpConfig>() {

    private val solutionFileField = ExtendableTextField()
    private val solutionLangModel = CollectionComboBoxModel<SolutionLanguage>()

    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFileField.text = s.solutionFilePath

        val settings = AutoCpSettings.instance
        solutionLangModel.replaceAll(settings.solutionLanguages)
        solutionLangModel.selectedItem = settings.getLangWithId(s.solutionLangId)
    }

    override fun applyEditorTo(s: AutoCpConfig) {
        s.solutionFilePath = solutionFileField.text

        val settings = AutoCpSettings.instance

        // verifies if selected lang still exists
        val selectedLang = settings.getLangWithId(solutionLangModel.selected?.id)
        s.solutionLangId = selectedLang?.id ?: -1
    }

    override fun createEditor(): JComponent {
        // add macro support
        MacrosDialog.addTextFieldExtension(solutionFileField)

        // browse button for executable field
        solutionFileField.addBrowseButton()

        // ui layout
        return panel {
            row("Solution File:") {
                solutionFileField()
                    .constraints(CCFlags.growX)
            }
            row("Solution Language:") {
                ComboBox(solutionLangModel)()
            }
        }
    }

    private fun ExtendableTextField.addBrowseButton() {
        val solutionFileDescriptor = FileChooserDescriptorFactory
            .createSingleFileDescriptor()

        this.addBrowseExtension({
            val preselectPath = Path.of(this.text.ifEmpty { project.basePath })
            val selectedFile = VfsUtil.findFile(preselectPath, true)
            FileChooser.chooseFile(solutionFileDescriptor, project, selectedFile) {
                this.text = it.path
            }
        }, this@ConfigEditor)
    }
}