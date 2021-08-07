package settings.langSettings.model

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.lang.Language
import kotlinx.serialization.Serializable
import lang.supportedFileTemplates
import common.ui.swing.TileCellRenderer

@Serializable
data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: Long?,
    val buildConfigs: List<BuildConfig>,
) {

    fun getLanguage() = Language.findLanguageByID(langId)

    fun getBuildConfig(): BuildConfig? {
        return buildConfigs.run {
            firstOrNull { it.id == defaultBuildConfigId } ?: firstOrNull()
        }
    }

    fun defaultFileTemplate(): FileTemplate? {
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