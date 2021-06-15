package database.models

@Deprecated("use ProblemState in dev.pushpavel.autocp.database")
data class ProblemState(
    val problemId: String,
    val selectedIndex: Int,
) {
    // constructor for creating new instances to be added to db
    @Deprecated("")
    constructor(selectedIndex: Int) : this("", selectedIndex)

    // constructor for creating default instance
    @Deprecated("")
    constructor() : this("", -1)
}
