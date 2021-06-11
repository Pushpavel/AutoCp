package database.models

data class TestcaseSpec(
    val id: Int,
    val name: String,
) {
    constructor(name: String) : this(1, name) // constructor for creating new instances to be added to db
}