package database.models

import database.tables.ProblemSpecs
import database.utils.decodedSplit
import org.jetbrains.exposed.sql.ResultRow

data class ProblemSpec(
    val name: String,
    val group: String
)