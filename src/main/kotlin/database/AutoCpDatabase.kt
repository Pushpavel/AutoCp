package database

import common.errors.Err
import database.models.Problem
import database.models.SolutionFile
import kotlinx.coroutines.flow.MutableStateFlow

class AutoCpDatabase(
    private val _problemsFlow: MutableStateFlow<Map<String, Map<String, Problem>>>,
    private val _solutionFilesFlow: MutableStateFlow<Map<String, SolutionFile>>
) {
    val problems get() = _problemsFlow.value
    val solutionFiles get() = _solutionFilesFlow.value

    fun updateProblem(problem: Problem) {
        val group = this.problems[problem.groupName]?.toMutableMap() ?: mutableMapOf()
        group[problem.name] = problem
        this._problemsFlow.value = problems.toMutableMap().apply { this[problem.groupName] = group }
    }

    fun addSolutionFile(path: String, linkedProblemId: Pair<String, String>?) {
        val solutionFile = if (linkedProblemId != null) {
            val problem = problems[linkedProblemId.first]?.get(linkedProblemId.second)
                ?: throw Err.InternalErr("trying to create solution File for non existing Problem specification")

            SolutionFile(
                path,
                linkedProblemId,
                problem.sampleTestcases.toList(),
                problem.memoryLimit,
                problem.timeLimit
            )
        } else
            SolutionFile(
                path,
                linkedProblemId,
                listOf()
            )

        _solutionFilesFlow.value = solutionFiles.toMutableMap().apply { this[path] = solutionFile }
    }

    fun updateSolutionFile(solutionFile: SolutionFile) {
        if (!solutionFiles.containsKey(solutionFile.pathString))
            throw Err.InternalErr("trying to update solution File which does not exist")

        _solutionFilesFlow.value = solutionFiles.toMutableMap().apply { this[solutionFile.pathString] = solutionFile }
    }
}