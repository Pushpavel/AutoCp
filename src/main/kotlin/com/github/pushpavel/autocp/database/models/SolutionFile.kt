package com.github.pushpavel.autocp.database.models

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable

@Serializable
data class SolutionFile(
    val pathString: String,
    val linkedProblemId: Pair<String, String>?,
    val testcases: List<Testcase>,
    val timeLimit: Long = 1000,
) {

    fun getLinkedProblem(project: Project): Problem? {
        return null
//        if (linkedProblemId == null) return null
//        return project.autoCp().problems[linkedProblemId.first]?.get(linkedProblemId.second)
    }
}