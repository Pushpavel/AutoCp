package database.models

import kotlinx.serialization.Serializable

@Serializable
data class SolutionFile(
    val pathString: String,
    val linkedProblemId: Pair<String, String>?,
    val testcases: List<Testcase>,
    val timeLimit: Long = 1000,
)