package database.models

import ui.StringCellRenderer

/**
 * Model class representing a single Testcase of a [Problem][com.github.pushpavel.autocp.database.Problem]
 */
data class Testcase(
    val name: String,
    val input: String,
    val output: String,
) {
    companion object {
        fun cellRenderer(): StringCellRenderer<Testcase> {
            return StringCellRenderer { it.name }
        }
    }
}