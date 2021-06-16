package database.models

import ui.StringCellRenderer

data class Testcase(
    val name: String,
    val input: String,
    val output: String,
) {
    companion object {
        fun cellRenderer(): StringCellRenderer<Testcase> {
            return StringCellRenderer<Testcase> { it.name }
        }
    }
}