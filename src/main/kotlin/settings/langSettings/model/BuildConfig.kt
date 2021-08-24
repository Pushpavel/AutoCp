package settings.langSettings.model

import com.intellij.icons.AllIcons
import common.res.R
import common.ui.swing.TileCellRenderer
import kotlinx.serialization.Serializable
import settings.generalSettings.AutoCpGeneralSettings

@Serializable
data class BuildConfig(
    val id: String,
    val name: String,
    val buildCommand: String,
    val executeCommand: String,
) {
    companion object {
        fun cellRenderer(emptyText: String = "None"): TileCellRenderer<BuildConfig> {
            return TileCellRenderer(emptyText = emptyText) {
                text = it.name
                icon = AllIcons.RunConfigurations.Applet
            }
        }
    }

    fun doesCommandHaveOutPath(): Boolean {
        return buildCommand.contains(AutoCpGeneralSettings.OUTPUT_PATH_KEY)
    }

    fun constructBuildCommand(inputPath: String, dirPath: String): String {
        return constructCommand(buildCommand, inputPath, dirPath)
    }

    fun constructExecuteCommand(inputPath: String, dirPath: String): String {
        return constructCommand(executeCommand, inputPath, dirPath)
    }

    private fun constructCommand(command: String, inputPath: String, dirPath: String): String {
        return command.replace(R.keys.inputPathMacro, "\"$inputPath\"")
            .replace(R.keys.dirUnquotedPathMacro, dirPath)
            .replace(R.keys.dirPathMacro, "\"$dirPath\"")
    }

    fun constructCommand(inputPath: String, outputPath: String? = null): String {
        return buildCommand
            .replace(AutoCpGeneralSettings.INPUT_PATH_KEY, "\"$inputPath\"")
            .let {
                if (outputPath != null)
                    it.replace(AutoCpGeneralSettings.OUTPUT_PATH_KEY, "\"$outputPath\"")
                else
                    it
            }
    }

    constructor(m: MutableBuildConfig) : this(m.id, m.name, m.buildCommand, m.executeCommand)
}


data class MutableBuildConfig(
    var id: String = "",
    var name: String = "",
    var buildCommand: String = "",
    var executeCommand: String = "",
) {
    constructor(c: BuildConfig) : this(c.id, c.name, c.buildCommand, c.executeCommand)
}