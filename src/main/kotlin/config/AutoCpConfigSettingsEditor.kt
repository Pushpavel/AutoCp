package config

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.Factory
import com.intellij.structuralsearch.plugin.ui.TextFieldWithAutoCompletionWithBrowseButton
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.layout.panel
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent

class AutoCpConfigSettingsEditor(private val project: @NotNull Project) : SettingsEditor<AutoCpConfig>() {

    val solutionFileChooser = TextFieldWithBrowseButton()
    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFileChooser.text = solutionFileChooser.text
    }

    override fun applyEditorTo(s: AutoCpConfig) {
        s.solutionFilePath = solutionFileChooser.text
    }

    override fun createEditor(): JComponent {
        val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("cpp")

        solutionFileChooser.addBrowseFolderListener(
            "Select Solution File",
            "Select the file containing the source code",
            project,
            fileChooserDescriptor
        )

        return panel {
            row {
                label("Solution File:")
                solutionFileChooser()
            }
        }
    }
}