package database.models

data class SolutionFile(
    val pathString: String,
    val linkedProblemId: Pair<String, String>?,
    val testcases: List<Testcase>
)