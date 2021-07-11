package settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "plugin.settings.AppSettingsState",
    storages = [Storage("autoCpPluginSettings.xml")]
)
@Service
data class AutoCpSettings(
    // TODO: make it private and access only by PreferredLang get/set
    // TODO: store id instead of name
    var preferredLanguage: String? = "C++",
    var solutionLanguages: MutableList<SolutionLanguage> = getDefaultSolutionLanguages(),
    var selectedIndex: Int? = 0, // TODO: Store -1 instead of null
) : PersistentStateComponent<AutoCpSettings> {

    fun getPreferredLang() = solutionLanguages.find { it.name == preferredLanguage }
    fun setPreferredLang(lang: SolutionLanguage?) {
        preferredLanguage = if (lang == null)
            null
        else
            solutionLanguages.find { it.name == lang.name }?.name
    }

    fun getLangWithId(id: Long?): SolutionLanguage? {
        if (id == null) return null
        return solutionLanguages.find { it.id == id }
    }

    override fun getState() = this

    override fun loadState(state: AutoCpSettings) = XmlSerializerUtil.copyBean(state, this)


    companion object {
        val instance = service<AutoCpSettings>()
        const val INPUT_PATH_KEY = "@input@"
        const val OUTPUT_PATH_KEY = "@output@"

        fun getSolutionLanguageTemplate(name: String?): SolutionLanguage {
            return SolutionLanguage(
                name ?: "C++",
                "cpp",
                "g++ $INPUT_PATH_KEY -o \"$OUTPUT_PATH_KEY\"",
                System.currentTimeMillis()
            )
        }

        fun getDefaultSolutionLanguages(): MutableList<SolutionLanguage> {
            return mutableListOf(
                SolutionLanguage("C++", "cpp", "g++ \"$INPUT_PATH_KEY\" -o \"$OUTPUT_PATH_KEY\" ", 1),
                SolutionLanguage("java", "java", "javac \"$INPUT_PATH_KEY\" --output \"$OUTPUT_PATH_KEY\" ", 2)
            )
        }
    }
}