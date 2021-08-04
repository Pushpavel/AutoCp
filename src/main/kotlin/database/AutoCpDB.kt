package database

import common.errors.Err
import database.models.Problem
import database.models.SolutionFile
import database.models.Testcase
import kotlinx.serialization.Serializable

@Serializable
data class AutoCpDB(
    val problems: MutableMap<String, MutableMap<String, Problem>> = mutableMapOf(),
    val solutionFiles: MutableMap<String, SolutionFile> = mutableMapOf(),
) {

    fun updateProblems(problems: List<Problem>) {
        if (problems.isEmpty()) return
        val groupName = problems[0].groupName
        val updateGroup = problems.associateBy { it.name }.toMutableMap()
        val group = this.problems[groupName]
        if (group != null)
            updateGroup.forEach { group[it.key] = it.value }
        else
            this.problems[groupName] = updateGroup
    }

    fun createSolutionFile(path: String, linkedProblemId: Pair<String, String>?) {
        var testcases: List<Testcase>? = null
        if (linkedProblemId != null) {
            val problem = problems[linkedProblemId.first]?.get(linkedProblemId.second)
                ?: throw Err.InternalErr("trying to create solution File for non existing Problem specification")

            testcases = problem.sampleTestcases.toList()
        }

        val solutionFile = SolutionFile(
            path,
            linkedProblemId,
            testcases ?: listOf()
        )

        solutionFiles[path] = solutionFile
    }
}