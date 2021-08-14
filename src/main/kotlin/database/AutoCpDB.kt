package database

import common.errors.Err
import database.models.Problem
import database.models.SolutionFile
import database.models.Testcase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutoCpDB(
    @SerialName("problems") private val _problems: MutableMap<String, MutableMap<String, Problem>> = mutableMapOf(),
    @SerialName("solutionFiles") private val _solutionFiles: MutableMap<String, SolutionFile> = mutableMapOf(),
) {

    val problems: Map<String, Map<String, Problem>> get() = _problems
    val solutionFiles: Map<String, SolutionFile> get() = _solutionFiles

    fun updateProblem(problem: Problem) {
        val group = this._problems[problem.groupName]
        if (group != null)
            group[problem.name] = problem
        else
            this._problems[problem.groupName] = mutableMapOf(Pair(problem.name, problem))
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

        _solutionFiles[path] = solutionFile
    }

    fun updateSolutionFile(solutionFile: SolutionFile) {
        if (!solutionFiles.containsKey(solutionFile.pathString))
            throw Err.InternalErr("trying to update solution File which does not exist")

        _solutionFiles[solutionFile.pathString] = solutionFile
    }
}