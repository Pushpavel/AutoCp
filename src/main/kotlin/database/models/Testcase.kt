package database.models

import kotlinx.serialization.Serializable
import common.ui.StringCellRenderer

/**
 * Model class representing a single Testcase of a [Problem][com.github.pushpavel.autocp.database.Problem]
 */
@Serializable
data class Testcase(
    val name: String,
    val input: String,
    val output: String,
) {
    companion object {
        fun cellRenderer(): StringCellRenderer<Testcase> {
            return StringCellRenderer { Pair(it.name, null) }
        }
    }
}