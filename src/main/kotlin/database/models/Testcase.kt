package database.models

import common.ui.StringCellRenderer
import kotlinx.serialization.Serializable

/**
 * Model class representing a single Testcase of a [Problem][database.models.Problem]
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