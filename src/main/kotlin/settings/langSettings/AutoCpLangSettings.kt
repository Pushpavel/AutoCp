package settings.langSettings

import com.intellij.lang.Language
import com.intellij.openapi.components.*
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.io.readText
import common.res.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import settings.langSettings.model.BuildConfig
import settings.langSettings.model.Lang
import settings.langSettings.model.MutableLang

@State(
    name = "settings.Languages",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpLangSettings : PersistentStateComponent<LangSettings> {

    var languages: List<Lang> = R.files.langJsons
        .map { it.readText() }
        .map<String, Lang> { Json.decodeFromString(it) }
        .filter { Language.findLanguageByID(it.langId) != null }

    override fun getState(): LangSettings {
        return LangSettings(languages.map { MutableLang(it) })
    }


    override fun loadState(state: LangSettings) {
        languages = state.languages.map { Lang(it) }
    }

    companion object {

        val instance: AutoCpLangSettings get() = service()

        fun guessBuildConfigById(id: String?, file: VirtualFile?): BuildConfig? {
            val lang = findLangByFile(file)
            // TODO: avoid guessing and look into build Configs of other languages too
            return lang?.getBuildConfig(id)
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