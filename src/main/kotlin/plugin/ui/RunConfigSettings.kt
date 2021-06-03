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
import org.jetbrains.annotations.NotNull
import plugin.config.AutoCpConfig
import java.nio.file.Path
import javax.swing.JComponent

class RunConfigSettings(private val project: @NotNull Project) : SettingsEditor<AutoCpConfig>() {

    val problemNameField = ExtendableTextField()
    val executableField = ExtendableTextField()

    override fun resetEditorFrom(s: AutoCpConfig) {
        problemNameField.text = s.problemName
        executableField.text = s.executablePath
    }

    override fun applyEditorTo(s: AutoCpConfig) {
        s.problemName = problemNameField.text
        s.executablePath = executableField.text
    }

    override fun createEditor(): JComponent {
        // add macro support
        MacrosDialog.addTextFieldExtension(executableField)
        MacrosDialog.addTextFieldExtension(problemNameField)

        // browse button for executable field
        val fileDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("exe")
        executableField.addBrowseExtension({

            val preselectPath = Path.of(executableField.text.ifEmpty { project.basePath })

            val selectedFile = VfsUtil.findFile(preselectPath, true)
            FileChooser.chooseFile(fileDescriptor, project, selectedFile) {
                executableField.text = it.path
            }

        }, this)


        // ui layout
        return panel {
            row("Problem Name:") {
                problemNameField()
                    .comment("This must match with solution file name without extension")
                    .constraints(CCFlags.growX)
            }
            row("Executable:") {
                executableField()
                    .constraints(CCFlags.growX)
            }
        }
    }
}