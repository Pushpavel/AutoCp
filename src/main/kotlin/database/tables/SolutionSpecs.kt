package database.tables

import database.models.ProblemInfo
import database.utils.encodedJoin
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object SolutionSpecs : Table() {
    val solutionPath = text("solutionPath").uniqueIndex()
    val problemId = text("problemId").references(ProblemSpecs.problemId)


    // operations on table

    fun insertModel(problemInfo: ProblemInfo, path: String) {
        insert {
            it[solutionPath] = path
            it[problemId] = encodedJoin(problemInfo.name, problemInfo.group)
        }
    }


    fun withSolutionPath(solutionPath: String): String? {
        return select { SolutionSpecs.solutionPath eq solutionPath }
            .firstOrNull()
            ?.let { it[problemId] }
    }
}
