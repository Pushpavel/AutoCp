package settings.langSettings

import com.intellij.openapi.components.*
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import common.res.R
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.model.MutableLang

@State(
    name = "settings.Languages",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpLangSettings : PersistentStateComponent<LangSettings> {

    var languages: List<Lang> = R.files.langJsons.map { Json.decodeFromString(it) }

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