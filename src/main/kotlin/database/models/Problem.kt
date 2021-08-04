package database.models

data class Problem(
    val name: String,
    val groupName: String,
    val url: String,
    val sampleTestcases: List<Testcase>
)
