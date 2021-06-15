package database.tables

import database.models.TestcaseSpec
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select

@Deprecated("exposed library will be removed soon")
object TestcaseSpecs : IntIdTable() {
    val name = varchar("name", 100)
    val input = text("input")
    val output = text("output")
    val problemId = reference("problemId", ProblemSpecs.problemId)


    // operations on table
    fun withProblemId(_problemId: String): List<TestcaseSpec> {
        return select { problemId eq _problemId }.map {
            TestcaseSpec(
                it[id].value,
                it[name],
                it[input],
                it[output],
                "",
                ""
            )
        }
    }

}