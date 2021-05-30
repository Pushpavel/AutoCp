package config

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.util.Factory
import com.intellij.ui.layout.panel
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent

class AutoCpConfigSettingsEditor : SettingsEditor<AutoCpConfig>() {

    override fun resetEditorFrom(s: AutoCpConfig) {

    }

    override fun applyEditorTo(s: AutoCpConfig) {

    }

    override fun createEditor(): JComponent {
        return panel {
            row { label("Super this is exactly where i want it to be") }
        }
    }
}