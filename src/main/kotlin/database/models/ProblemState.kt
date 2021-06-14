package database.models

data class ProblemState(
    val problemId: String,
    val selectedIndex: Int,
) {
    // constructor for creating new instances to be added to db
    constructor(selectedIndex: Int) : this("", selectedIndex)

    // constructor for creating default instance
    constructor() : this("", -1)
}
