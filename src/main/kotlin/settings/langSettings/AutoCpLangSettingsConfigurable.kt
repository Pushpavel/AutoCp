package settings.langSettings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.DumbAware
import common.isItemsEqual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import settings.langSettings.ui.LangSettingsView
import settings.langSettings.ui.LangSettingsViewModel
import javax.swing.JComponent

class AutoCpLangSettingsConfigurable : Configurable, DumbAware {
    lateinit var scope: CoroutineScope
    lateinit var model: LangSettingsViewModel

    override fun createComponent(): JComponent {
        scope = MainScope()
        model = LangSettingsViewModel()

        return LangSettingsView().apply {
            bindToViewModel(scope, model)
        }
    }

    override fun isModified(): Boolean {
        val languages = AutoCpLangSettings.getLanguages()
        return !model.languages.value.isItemsEqual(languages)
    }

    override fun apply() {
        AutoCpLangSettings.setLanguages(model.languages.value)
    }

    override fun reset() {
        val languages = AutoCpLangSettings.getLanguages()
        model.languages.tryEmit(languages)
    }

    override fun getDisplayName() = "Languages"
}