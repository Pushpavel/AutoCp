package database.tables

import org.jetbrains.exposed.sql.Table

object SolutionSpecs : Table() {
    val solutionPath = text("solutionPath").uniqueIndex()
    val problemId = text("problemId").references(ProblemSpecs.problemId)
}