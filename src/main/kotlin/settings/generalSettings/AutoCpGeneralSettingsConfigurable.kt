package settings.generalSettings

import com.intellij.openapi.options.Configurable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import settings.generalSettings.ui.GeneralSettingsView
import settings.generalSettings.ui.GeneralSettingsViewModel
import settings.langSettings.AutoCpLangSettings
import ui.helpers.mainScope

class AutoCpGeneralSettingsConfigurable : Configurable {

    val scope = mainScope()
    val model = GeneralSettingsViewModel(scope)
    private val view = GeneralSettingsView(model)

    override fun createComponent() = view

    override fun isModified(): Boolean {
        val langId = AutoCpGeneralSettings.getPreferredLangId()
        val selectedLangId = model.langList.value.getOrNull(model.selectedLangIndex.value)?.langId
        return selectedLangId != langId
    }

    override fun apply() {
        val selectedLangId = model.langList.value.getOrNull(model.selectedLangIndex.value)?.langId
        AutoCpGeneralSettings.setPreferredLangId(selectedLangId)
    }

    override fun reset() {
        val langId = AutoCpGeneralSettings.getPreferredLangId()
        val languages = AutoCpLangSettings.getLanguages()
        scope.launch {
            model.langList.emit(languages)
            model.selectedLangIndex.emit(languages.indexOfFirst { it.langId == langId })
        }
    }

    override fun disposeUIResources() {
        scope.cancel()
    }

    override fun getDisplayName() = "AutoCp"
}