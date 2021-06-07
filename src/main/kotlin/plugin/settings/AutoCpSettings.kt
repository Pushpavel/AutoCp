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
    var solutionLanguages: MutableList<SolutionLanguage> = getDefaultSolutionLanguages()
    var selectedIndex: Int? = 0

    override fun getState() = this

    override fun loadState(state: AutoCpSettings) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance = service<AutoCpSettings>()

        val DUPLICATE_NAME_REGEX = Regex("^(.*)_([0-9]+)\$")

        fun getSolutionLanguageTemplate(): SolutionLanguage {
            return SolutionLanguage(
                "lang",
                "extension",
                "command"
            )
        }

        fun getDefaultSolutionLanguages(): MutableList<SolutionLanguage> {
            return mutableListOf(
                SolutionLanguage("C++", "cpp", "g++ !input_file! -o !output_file! "),
                SolutionLanguage("java", "java", "javac !input_file! --output !output_file! ")
            )
        }
    }
}