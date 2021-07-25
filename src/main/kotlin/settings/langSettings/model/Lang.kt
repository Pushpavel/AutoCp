package settings.langSettings.model

import com.intellij.lang.Language
import ui.StringCellRenderer

data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: Long?,
    val buildConfigs: List<BuildConfig>,
) {
    companion object {
        fun cellRenderer(): StringCellRenderer<Lang> {
            return StringCellRenderer {
                val lang = Language.findLanguageByID(it.langId)
                if (lang == null) null else
                    Pair(lang.displayName, lang.associatedFileType?.icon)
            }
        }
    }
}