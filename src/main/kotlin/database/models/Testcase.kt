package database.models

import common.ui.swing.TileCellRenderer
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
        fun cellRenderer() = TileCellRenderer<Testcase> { text = it.name }
    }
}