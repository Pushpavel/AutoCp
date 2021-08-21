package settings.langSettings.model

import com.intellij.icons.AllIcons
import common.ui.swing.TileCellRenderer
import kotlinx.serialization.Serializable
import settings.generalSettings.AutoCpGeneralSettings

@Serializable
data class BuildConfig(
    val id: String,
    val name: String,
    val commandTemplate: String
) {
    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<BuildConfig> {
            return TileCellRenderer(emptyText = emptyText) {
                text = it.name
                icon = AllIcons.RunConfigurations.Applet
            }
        }
    }

    fun constructCommand(inputPath: String, outputPath: String): String {
        return commandTemplate
            .replace(AutoCpGeneralSettings.INPUT_PATH_KEY, "\"$inputPath\"")
            .replace(AutoCpGeneralSettings.OUTPUT_PATH_KEY, "\"$outputPath\"")
    }

    constructor(m: MutableBuildConfig) : this(m.id, m.name, m.commandTemplate)
}


data class MutableBuildConfig(
    var id: String = "",
    var name: String = "",
    var commandTemplate: String = "",
) {
    constructor(c: BuildConfig) : this(c.id, c.name, c.commandTemplate)
}