package settings.generalSettings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.panel
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.Lang

class AutoCpGeneralSettingsConfigurable : BoundConfigurable("AutoCp") {

    private val langSettings = AutoCpLangSettings.instance
    private val generalSettings = AutoCpGeneralSettings.instance
    private val langModel = CollectionComboBoxModel<Lang>()

    override fun createPanel() = panel {
        onGlobalReset {
            langModel.replaceAll(langSettings.languages)
        }

        row("Preferred Language") {
            comboBox<Lang?>(
                langModel,
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