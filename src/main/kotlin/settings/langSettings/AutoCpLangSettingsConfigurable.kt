package settings.langSettings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.DumbAware
import kotlinx.coroutines.MainScope
import settings.langSettings.ui.LangSettingsView
import settings.langSettings.ui.LangSettingsViewModel
import javax.swing.JComponent

class AutoCpLangSettingsConfigurable : Configurable, DumbAware {
    override fun createComponent(): JComponent {
        return LangSettingsView().apply {
            bindToViewModel(MainScope(), LangSettingsViewModel())
        }
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun getDisplayName(): String {
        return "Where Is This"
    }
}