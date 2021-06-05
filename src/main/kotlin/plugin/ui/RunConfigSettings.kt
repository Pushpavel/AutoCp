package plugin.ui

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import common.Constants
import org.jetbrains.annotations.NotNull
import plugin.config.AutoCpConfig
import java.nio.file.Path
import javax.swing.JComponent

class RunConfigSettings(private val project: @NotNull Project) : SettingsEditor<AutoCpConfig>() {

    val solutionFileField = ExtendableTextField()
    val executableField = ExtendableTextField()

    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFileField.text = s.solutionFilePath
        executableField.text = s.executablePath
    }

    override fun applyEditorTo(s: AutoCpConfig) {
        s.solutionFilePath = solutionFileField.text
        s.executablePath = executableField.text
    }

    override fun createEditor(): JComponent {
        // add macro support
        MacrosDialog.addTextFieldExtension(executableField)
        MacrosDialog.addTextFieldExtension(solutionFileField)

        // browse button for executable field
        executableField.addBrowseButton(listOf("exe"))
        solutionFileField.addBrowseButton(Constants.SupportedSolutionFileExtensions)

        // ui layout
        return panel {
            row("Solution File:") {
                solutionFileField()
                    .constraints(CCFlags.growX)
            }
            row("Executable:") {
                executableField()
                    .constraints(CCFlags.growX)
            }
        }
    }

    private fun ExtendableTextField.addBrowseButton(extensions: List<String>) {
        val solutionFileDescriptor = FileChooserDescriptorFactory
            .createSingleFileDescriptor()
            .withFileFilter { it.extension in extensions }

        this.addBrowseExtension({
            val preselectPath = Path.of(this.text.ifEmpty { project.basePath })
            val selectedFile = VfsUtil.findFile(preselectPath, true)
            FileChooser.chooseFile(solutionFileDescriptor, project, selectedFile) {
                this.text = it.path
            }
        }, this@RunConfigSettings)
    }
}