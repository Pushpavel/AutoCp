package settings.langSettings.model

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.lang.Language
import com.jetbrains.rd.util.firstOrNull
import gather.supportedFileTemplates
import common.ui.swing.TileCellRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: String?,
    var buildConfigs: Map<String, BuildConfig>,
) {

    fun getLanguage() = Language.findLanguageByID(langId)

    fun getBuildConfig(id: String?): BuildConfig? {
        return buildConfigs.run { this[id] ?: firstOrNull()?.value }
    }

    fun getDefaultBuildConfig(): BuildConfig? {
        return getBuildConfig(defaultBuildConfigId)
    }

    fun getFileTemplate(): FileTemplate? {
        return supportedFileTemplates().run {
            firstOrNull { it.name == fileTemplateName } ?: firstOrNull()
        }
    }

    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<Lang> {
            return TileCellRenderer(emptyText = emptyText) {
                Language.findLanguageByID(it.langId)?.apply {
                    text = displayName
                    icon = associatedFileType?.icon
                }
            }
        }
    }

    constructor(m: MutableLang) : this(
        m.langId,
        m.fileTemplateName,
        m.defaultBuildConfigId,
        m.buildConfigs.mapValues { BuildConfig(it.value) }
    )
}

data class MutableLang(
    var langId: String = "",
    var fileTemplateName: String? = null,
    var defaultBuildConfigId: String? = null,
    var buildConfigs: Map<String, MutableBuildConfig> = mapOf(),
) {
    constructor(lang: Lang) : this(
        lang.langId,
        lang.fileTemplateName,
        lang.defaultBuildConfigId,
        lang.buildConfigs.mapValues { MutableBuildConfig(it.value) }
    )
}