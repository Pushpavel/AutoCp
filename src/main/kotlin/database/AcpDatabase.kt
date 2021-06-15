package database

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.models.ProblemInfo
import database.models.OldProblemSpec
import dev.pushpavel.autocp.database.AutoCpDatabase

@Service
class AcpDatabase(project: Project) {

    private val db: AutoCpDatabase

    init {
        val driver = JdbcSqliteDriver("jdbc:sqlite:${project.basePath}/.autocp")
        db = AutoCpDatabase(driver)

    }

    fun insertProblemSpecs(specs: List<OldProblemSpec>): Result<Unit> {
        try {

            db.transaction {
                for (spec in specs) {
                    db.problemQueries.insertProblemSpec(spec.toProblem())

                    for (testcaseSpec in spec.testcases)
                        db.testcaseQueries.insertTestcase(testcaseSpec.toTestcase())
                }
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    fun associateSolutionToProblem(solutionPath: String, problemInfo: ProblemInfo): Result<Unit> {
        try {
            db.transaction {
                db.solutionProblemQueries.associateSolutionToProblem(
                    solutionPath,
                    problemInfo.name,
                    problemInfo.group
                )
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    fun getProblemSpec(solutionPath: String): Result<Unit> {
        return try {
            val spec = db.transaction {
                val relation = db.solutionProblemQueries.getProblemSolution(solutionPath).executeAsOne()
                val problem = db.problemQueries.getProblem(relation.problemName, relation.problemGroup).executeAsOne()
                val testcases = db.testcaseQueries.getTestcases(relation.problemName, relation.problemGroup)
                    .executeAsList()

            }
            return Result.success(spec)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}