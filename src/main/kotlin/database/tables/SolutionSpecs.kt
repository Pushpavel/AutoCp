package database.tables

import database.models.ProblemSpec
import database.utils.encodedJoin
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

object SolutionSpecs : Table() {
    val solutionPath = text("solutionPath").uniqueIndex()
    val problemId = text("problemId").references(ProblemSpecs.problemId)


    // operations on table

    fun insertModel(problemSpec: ProblemSpec, path: String) {
        insert {
            it[solutionPath] = path
            it[problemId] = encodedJoin(problemSpec.name, problemSpec.group)
        }
    }


    fun withSolutionPath(solutionPath: String): String? {
        return select { SolutionSpecs.solutionPath eq solutionPath }
            .firstOrNull()
            ?.let { it[problemId] }
    }
}
