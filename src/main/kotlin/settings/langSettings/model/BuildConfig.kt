package settings.langSettings.model

import com.intellij.icons.AllIcons
import settings.generalSettings.AutoCpGeneralSettings
import ui.swing.TileCellRenderer

data class BuildConfig(
    val id: Long,
    val name: String,
    val buildCommand: String
) {
    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<BuildConfig> {
            return TileCellRenderer(emptyText = emptyText) {
                text = it.name
                icon = AllIcons.RunConfigurations.Applet
            }
        }
    }

    fun constructBuildCommand(inputPath: String, outputPath: String): String {
        return buildCommand
            .replace(AutoCpGeneralSettings.INPUT_PATH_KEY, inputPath)
            .replace(AutoCpGeneralSettings.OUTPUT_PATH_KEY, outputPath)
    }
}