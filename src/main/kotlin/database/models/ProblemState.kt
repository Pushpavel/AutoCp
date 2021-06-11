package database.models

data class ProblemState(
    val problemId: String,
    val selectedIndex: Int,
) {
    constructor(selectedIndex: Int) : this("", selectedIndex) // constructor for creating new instances to be added to db
}
