package settings.langSettings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.Disposer
import common.isItemsEqual
import common.isNotIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import settings.langSettings.ui.langSettings.LangSettingsView
import settings.langSettings.ui.langSettings.LangSettingsViewModel
import ui.helpers.mainScope
import javax.swing.JComponent

class AutoCpLangSettingsConfigurable : Configurable, DumbAware {
    lateinit var scope: CoroutineScope
    lateinit var model: LangSettingsViewModel

    override fun createComponent(): JComponent {
        scope = mainScope()
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
        runBlocking {
            model.languages.emit(languages)
            if (languages.isNotIndex(model.selectedLangIndex.value) && languages.isNotEmpty()) {
                model.selectedLangIndex.emit(0)
            }
        }
    }

    override fun disposeUIResources() {
        Disposer.dispose(model)
        scope.cancel()
    }

    override fun getDisplayName() = "Languages"
}