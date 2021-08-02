package settings.langSettings

import com.intellij.openapi.components.*
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.vfs.VirtualFile
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang

@State(
    name = "plugin.settings.LangSettingsState",
    storages = [Storage("autoCpLangSettings.xml")]
)
@Service
class AutoCpLangSettings : PersistentStateComponent<AutoCpLangSettings> {

    var languages: List<Lang> = listOf(
        Lang("ObjectiveC", null, -1, listOf(BuildConfig(10, "C++ 17", "g++ build '@input@' '@output@'"))),
    ) // TODO: Initialize with defaults

    override fun getState() = this

    override fun loadState(state: AutoCpLangSettings) {
        languages = state.languages
    }

    companion object {
        fun getLanguages() = service<AutoCpLangSettings>().languages
        fun setLanguages(languages: List<Lang>) {
            service<AutoCpLangSettings>().languages = languages
        }

        fun findBuildConfigById(id: Long): BuildConfig? {
            val languages = getLanguages()
            for (lang in languages)
                for (config in lang.buildConfigs)
                    if (config.id == id)
                        return config

            return null
        }

        fun findLangByFile(file: VirtualFile?): Lang? {
            val fileType = file?.fileType ?: return null
            if (fileType is UnknownFileType || fileType !is LanguageFileType)
                return null

            val langId = fileType.language.id

            return getLanguages().firstOrNull {
                it.langId == langId
            }
        }
    }
}