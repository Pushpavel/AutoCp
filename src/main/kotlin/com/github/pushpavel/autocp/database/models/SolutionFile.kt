package com.github.pushpavel.autocp.database.models

import com.github.pushpavel.autocp.database.AutoCpDatabase
import kotlinx.serialization.Serializable

@Serializable
data class SolutionFile(
    val pathString: String,
    val linkedProblemId: Pair<String, String>?,
    val testcases: List<Testcase>,
    val timeLimit: Long = 1000,
) {

    fun getLinkedProblem(db: AutoCpDatabase): Problem? {
        if (linkedProblemId == null) return null
        return db.problems[linkedProblemId.first]?.get(linkedProblemId.second)
    }
}