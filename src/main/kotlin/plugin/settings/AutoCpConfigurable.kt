package plugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel

class AutoCpConfigurable : Configurable {
    private var ui: AutoCpSettingsUI? = null
    override fun createComponent(): DialogPanel {
        AutoCpSettingsUI().also {
            ui = it
            return it.component
        }
    }

    override fun isModified(): Boolean {
        val settings = AutoCpSettings.instance
        return settings.testText != ui?.text?.text
    }

    override fun apply() {

        val settings = AutoCpSettings.instance
        ui?.let {
            settings.testText = it.text.text
        }
    }

    override fun reset() {
        val settings = AutoCpSettings.instance
        ui?.let {
            it.text.text = settings.testText
        }
    }

    override fun disposeUIResources() {
        ui = null
    }

    override fun getDisplayName() = "AutoCp Build Tools"
}