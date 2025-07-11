package com.github.pushpavel.autocp.database

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.database.models.JudgeSettings
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.database.models.SolutionFile
import kotlinx.serialization.json.*

class UnknownVersionErr(val version: Int?, message: String?): Exception(message)

class Migrations {

    companion object {

        private val migrations: List<Migration> = listOf(
            MIGRATE_VERSION_1,
            object: Migration(setOf(R.keys.autoCpFileVersionNumber)) {}
        )

        fun migrateDB(json: JsonElement): AutoCpDB {
            val version = json.jsonObject["version"]?.jsonPrimitive?.int
            val problems = json.jsonObject["problems"]?.jsonObject
            val solutionsFiles = json.jsonObject["solutionFiles"]?.jsonObject
            val migration = migrations.firstOrNull { it.getVersions().contains(version) }
            if (migration == null)
                throw UnknownVersionErr(version, "Don't know how to migrate from version $version to ${R.keys.autoCpFileVersionNumber}")
            return AutoCpDB(
                R.keys.autoCpFileVersionNumber,
                problems?.let { it.entries.associate { (k, v) ->
                    k to v.jsonObject.entries.associate { (k, v) ->
                        k to migration.migrateProblem(v)
                    }
                } } ?: mapOf(),
                solutionsFiles?.let { it.entries.associate { (k, v) ->
                    k to migration.migrateSolutionFile(v)
                } } ?: mapOf(),
            )
        }

    }

}

abstract class Migration(private val versions: Set<Int>) {

    open fun migrateProblem(problem: JsonElement): Problem = Json.decodeFromJsonElement(problem)

    open fun migrateSolutionFile(solutionFile: JsonElement): SolutionFile = Json.decodeFromJsonElement(solutionFile)

    fun getVersions(): Set<Int> = versions

}

val MIGRATE_VERSION_1 = object: Migration(setOf(1)) {

    override fun migrateProblem(problem: JsonElement): Problem {
        return Problem(
            name = problem.jsonObject["name"]?.jsonPrimitive?.content ?: "",
            groupName = problem.jsonObject["groupName"]?.jsonPrimitive?.content ?: "",
            url = problem.jsonObject["url"]?.jsonPrimitive?.content ?: "",
            sampleTestcases = problem.jsonObject["sampleTestcases"]?.jsonArray?.map { Json.decodeFromJsonElement(it) } ?: listOf(),
            memoryLimit = problem.jsonObject["memoryLimit"]?.jsonPrimitive?.long ?: 256,
            timeLimit = problem.jsonObject["timeLimit"]?.jsonPrimitive?.long ?: 1000,
            inputFile = null,
            outputFile = null
        )
    }

    override fun migrateSolutionFile(solutionFile: JsonElement): SolutionFile {
        return SolutionFile(
            pathString = solutionFile.jsonObject["pathString"]!!.jsonPrimitive.content,
            linkedProblemId = solutionFile.jsonObject["linkedProblemId"]?.let {
                Json.decodeFromJsonElement(it)
            },
            testcases = solutionFile.jsonObject["testcases"]?.jsonArray?.map { Json.decodeFromJsonElement(it) } ?: listOf(),
            judgeSettings = JudgeSettings(
                timeLimit = solutionFile.jsonObject["timeLimit"]?.jsonPrimitive?.long ?: 1000,
            )
        )
    }

}
