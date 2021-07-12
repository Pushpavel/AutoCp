package settings.langSettings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.PlatformUtils
import settings.langSettings.model.Lang

@State(
    name = "plugin.settings.BuildSettingsState",
    storages = [Storage("autoCpLangSettings.xml")]
)
@Service
class AutoCpLangSettings : PersistentStateComponent<List<Lang>> {

    private var languages: List<Lang> = ArrayList() // TODO: Initialize with defaults

    override fun getState() = languages

    override fun loadState(state: List<Lang>) {
        languages = state
    }

    companion object {
        fun getLanguages() = service<AutoCpLangSettings>().languages
    }
}