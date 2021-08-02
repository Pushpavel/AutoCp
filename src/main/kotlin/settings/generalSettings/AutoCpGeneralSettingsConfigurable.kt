package settings.generalSettings

import com.intellij.openapi.options.Configurable
import settings.generalSettings.ui.GeneralSettingsView
import settings.langSettings.AutoCpLangSettings

class AutoCpGeneralSettingsConfigurable : Configurable {

    private val view = GeneralSettingsView()

    override fun createComponent() = view

    override fun isModified(): Boolean {
        val langId = AutoCpGeneralSettings.getPreferredLangId()
        println("${view.selectedLang?.langId} == $langId")
        return view.selectedLang?.langId != langId
    }

    override fun apply() {
        val selectedLangId = view.selectedLang?.langId
        AutoCpGeneralSettings.setPreferredLangId(selectedLangId)
    }

    override fun reset() {
        val langId = AutoCpGeneralSettings.getPreferredLangId()
        val languages = AutoCpLangSettings.getLanguages()
        view.langModel.replaceAll(languages)
        view.selectedLang = languages.firstOrNull { it.langId == langId }
    }

    override fun getDisplayName() = "AutoCp"
}