package plugin.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "plugin.settings.AppSettingsState",
    storages = [Storage("autoCpPluginSettings.xml")]
)
@Service
class AutoCpSettings : PersistentStateComponent<AutoCpSettings> {
    var preferredLanguage: String? = "cpp"
    var solutionLanguages = mutableListOf(
        SolutionLanguage("cpp", "g++", ".cpp"),
        SolutionLanguage("java", "javac", ".java")
    )
    var selectedIndex: Int = 0

    override fun getState() = this

    override fun loadState(state: AutoCpSettings) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance = service<AutoCpSettings>()
    }
}