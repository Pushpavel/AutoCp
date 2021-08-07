package settings.langSettings.model

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.lang.Language
import common.lang.supportedFileTemplates
import common.ui.swing.TileCellRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: Long?,
    val buildConfigs: List<BuildConfig>,
) {

    fun getLanguage() = Language.findLanguageByID(langId)

    fun getBuildConfig(id: Long?): BuildConfig? {
        return buildConfigs.run {
            firstOrNull { it.id == id } ?: firstOrNull()
        }
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
        m.buildConfigs.map { BuildConfig(it) }
    )
}

data class MutableLang(
    var langId: String = "",
    var fileTemplateName: String? = null,
    var defaultBuildConfigId: Long? = null,
    var buildConfigs: List<MutableBuildConfig> = listOf(),
) {
    constructor(lang: Lang) : this(
        lang.langId,
        lang.fileTemplateName,
        lang.defaultBuildConfigId,
        lang.buildConfigs.map { MutableBuildConfig(it) }
    )
}