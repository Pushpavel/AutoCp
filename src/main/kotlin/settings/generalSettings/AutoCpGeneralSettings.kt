package settings.generalSettings

import com.intellij.openapi.components.*
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.Lang

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "settings.General",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpGeneralSettings : PersistentStateComponent<AutoCpGeneralSettings> {
    var preferredLangId: String? = AutoCpLangSettings.instance.languages.firstOrNull()?.langId
    var shouldStartGatheringOnStart = true


    fun getPreferredLang(): Lang? {
        return AutoCpLangSettings.instance.languages.run {
            if (preferredLangId != null)
                firstOrNull { it.langId == preferredLangId } ?: firstOrNull()
            else
                firstOrNull()
        }
    }

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        preferredLangId = state.preferredLangId
    }

    companion object {
        // TODO: move this somewhere else
        const val INPUT_PATH_KEY = "@in"
        const val OUTPUT_PATH_KEY = "@out"

        val instance: AutoCpGeneralSettings get() = service()
    }
}