package database.tables

import org.jetbrains.exposed.dao.id.IntIdTable


object TestcaseSpecs : IntIdTable() {
    val name = varchar("name", 100)
    val problemId = reference("problemId", ProblemSpecs.problemId)
}