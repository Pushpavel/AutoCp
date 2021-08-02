package settings.langSettings.model

import com.intellij.lang.Language
import ui.swing.TileCellRenderer

data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: Long?,
    val buildConfigs: List<BuildConfig>,
) {
    companion object {
        fun cellRenderer(emptyValue: String = "None"): TileCellRenderer<Lang?> {
            return TileCellRenderer {
                if (it != null)
                    Language.findLanguageByID(it.langId)?.apply {
                        text = displayName
                        icon = associatedFileType?.icon
                    }
                else
                    text = emptyValue
            }
        }
    }
}