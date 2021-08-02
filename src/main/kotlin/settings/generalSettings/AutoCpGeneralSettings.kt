package settings.generalSettings

import com.intellij.openapi.components.*

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "plugin.settings.GeneralSettingsState",
    storages = [Storage("autoCpGeneralSettings.xml")]
)
@Service
class AutoCpGeneralSettings : PersistentStateComponent<AutoCpGeneralSettings> {
    var preferredLangId: String? = null

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        preferredLangId = state.preferredLangId
    }

    companion object {
        // TODO: move this somewhere else
        const val INPUT_PATH_KEY = "@input@"
        const val OUTPUT_PATH_KEY = "@output@"

        private val instance: AutoCpGeneralSettings
            get() = service()

        fun getPreferredLangId(): String? {
            return instance.preferredLangId
        }

        fun setPreferredLangId(langId: String?) {
            instance.preferredLangId = langId
        }
    }
}