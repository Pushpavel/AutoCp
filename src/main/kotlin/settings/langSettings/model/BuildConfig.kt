package settings.langSettings.model

import com.intellij.icons.AllIcons
import ui.StringCellRenderer

data class BuildConfig(
    val id: Long,
    val name: String,
    val buildCommand: String
) {
    companion object {
        fun cellRenderer(): StringCellRenderer<BuildConfig> {
            return StringCellRenderer {
                Pair(it.name, AllIcons.RunConfigurations.Applet)
            }
        }
    }
}