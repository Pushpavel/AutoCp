package plugin.config

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import org.jetbrains.annotations.NotNull
import javax.swing.JComponent

class AutoCpConfigSettingsEditor(private val project: @NotNull Project) : SettingsEditor<AutoCpConfig>() {

    val solutionFileChooser = ExtendableTextField()
    val runCommandField = ExtendableTextField()
    override fun resetEditorFrom(s: AutoCpConfig) {
        solutionFileChooser.text = s.problemName
        runCommandField.text = s.runCommand
    }

    override fun applyEditorTo(s: AutoCpConfig) {
        s.problemName = solutionFileChooser.text
        s.runCommand = runCommandField.text
    }

    override fun createEditor(): JComponent {
        MacrosDialog.addTextFieldExtension(runCommandField)

        return panel {
            row("Solution Name:") {
                solutionFileChooser()
                    .comment("This must match with solution file name without extension")
                    .constraints(CCFlags.growX)
            }
            row("Run Command:") {
                runCommandField().constraints(CCFlags.growX)
            }
        }
    }
}