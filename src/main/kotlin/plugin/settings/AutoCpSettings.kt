package plugin.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "plugin.settings.AppSettingsState",
    storages = [Storage("autoCpPluginSettings.xml")]
)
@Service
class AutoCpSettings : PersistentStateComponent<AutoCpSettings> {
    var testText = "Testing"

    override fun getState(): AutoCpSettings {
        return this
    }

    override fun loadState(state: AutoCpSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance = service<AutoCpSettings>()
    }
}