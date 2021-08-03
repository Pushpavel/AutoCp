package settings.langSettings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.DumbAware
import common.isItemsEqual
import common.isNotIndex
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import settings.langSettings.ui.langSettings.LangSettingsView
import settings.langSettings.ui.langSettings.LangSettingsViewModel
import ui.helpers.mainScope
import javax.swing.JComponent

class AutoCpLangSettingsConfigurable : Configurable, DumbAware {
    val scope = mainScope()
    val model = LangSettingsViewModel(scope)
    val langSettings = AutoCpLangSettings.instance

    override fun createComponent(): JComponent {
        return LangSettingsView(model)
    }

    override fun isModified(): Boolean {
        return !model.languages.value.isItemsEqual(langSettings.languages)
    }

    override fun apply() {
        langSettings.languages = model.languages.value
    }

    override fun reset() {
        val languages = langSettings.languages
        runBlocking {
            model.languages.emit(languages)
            if (languages.isNotIndex(model.selectedLangIndex.value) && languages.isNotEmpty()) {
                model.selectedLangIndex.emit(0)
            }
        }
    }

    override fun disposeUIResources() = scope.cancel()

    override fun getDisplayName() = "Languages"
}