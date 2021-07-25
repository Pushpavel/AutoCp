package settings.generalSettings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "plugin.settings.GeneralSettingsState",
    storages = [Storage("autoCpGeneralSettings.xml")]
)
@Service
class AutoCpGeneralSettings : PersistentStateComponent<AutoCpGeneralSettings> {
    private var preferredLangId: Long = -1

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        preferredLangId = state.preferredLangId
    }

    companion object {
        // TODO: move this somewhere else
        const val INPUT_PATH_KEY = "@input@"
        const val OUTPUT_PATH_KEY = "@output@"
    }
}