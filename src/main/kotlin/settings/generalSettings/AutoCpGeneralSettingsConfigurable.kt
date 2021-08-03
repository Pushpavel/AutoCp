package settings.generalSettings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.layout.panel
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.Lang
import ui.dsl.simpleComboBoxView

class AutoCpGeneralSettingsConfigurable : BoundConfigurable("AutoCp") {

    private val langSettings = AutoCpLangSettings.instance
    private val generalSettings = AutoCpGeneralSettings.instance

    override fun createPanel() = panel {

        row("Preferred Language") {
            simpleComboBoxView(
                langSettings.languages,
                {
                    langSettings.languages.firstOrNull {
                        it.langId == generalSettings.preferredLangId
                    }
                },
                {
                    generalSettings.preferredLangId = it?.langId
                },
                Lang.cellRenderer()
            )
        }
    }
}