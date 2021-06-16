package settings

import ui.StringCellRenderer

data class SolutionLanguage(
    var name: String = "",
    var extension: String = "",
    var buildCommand: String = "",
    var id: Long = 0L,
) {

    fun buildCommandString(inputPath: String, outputPath: String): String {
        return buildCommand
            .replace(AutoCpSettings.INPUT_PATH_KEY, inputPath)
            .replace(AutoCpSettings.OUTPUT_PATH_KEY, outputPath)
    }

    companion object {
        fun cellRenderer(): StringCellRenderer<SolutionLanguage> {
            return StringCellRenderer { it.name }
        }
    }
}