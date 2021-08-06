package settings.langSettings

import com.intellij.openapi.components.*
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.vfs.VirtualFile
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.model.MutableLang

@State(
    name = "settings.Languages",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpLangSettings : PersistentStateComponent<LangSettings> {

    var languages: List<Lang> = listOf(
        Lang("ObjectiveC", null, -1, listOf(BuildConfig(10, "C++ 17", "g++ build '@input@' '@output@'"))),
//        Lang("Python", null, -1, listOf(BuildConfig(10, "python 3.8", "python build '@input@' '@output@'"))),
    ) // TODO: Initialize with defaults

    override fun getState(): LangSettings {
        return LangSettings(languages.map { MutableLang(it) })
    }


    override fun loadState(state: LangSettings) {
        languages = state.languages.map { Lang(it) }
    }

    companion object {

        val instance: AutoCpLangSettings get() = service()

        fun findBuildConfigById(id: Long): BuildConfig? {
            for (lang in instance.languages)
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

            return instance.languages.firstOrNull {
                it.langId == langId
            }
        }
    }
}

data class LangSettings(
    var languages: List<MutableLang> = listOf()
)