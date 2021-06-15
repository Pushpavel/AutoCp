package database.tables

import org.jetbrains.exposed.sql.Table

@Deprecated("exposed library will be removed soon")
object ProblemSpecs : Table() {

    val problemId = text("problemId").uniqueIndex()

    // state
    val selectedIndex = integer("state_selectedIndex")

    override val primaryKey = PrimaryKey(problemId)
}