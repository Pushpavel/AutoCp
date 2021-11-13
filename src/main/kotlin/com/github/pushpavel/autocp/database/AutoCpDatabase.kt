package com.github.pushpavel.autocp.database

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.database.models.SolutionFile
import kotlinx.coroutines.flow.MutableStateFlow

class AutoCpDatabase(
    val problemsFlow: MutableStateFlow<Map<String, Map<String, Problem>>>,
    val solutionFilesFlow: MutableStateFlow<Map<String, SolutionFile>>
) {
    val problems get() = problemsFlow.value

    fun updateProblem(problem: Problem) {
        val group = this.problems[problem.groupName]?.toMutableMap() ?: mutableMapOf()
        group[problem.name] = problem
        this.problemsFlow.value = problems.toMutableMap().apply { this[problem.groupName] = group }
    }

    fun modifySolutionFiles(action: MutableMap<String, SolutionFile>.() -> Unit) {
        solutionFilesFlow.value = solutionFilesFlow.value.toMutableMap().apply(action)
    }
}