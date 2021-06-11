package database.models

data class TestcaseSpec(
    val id: Int,
    val name: String,
    val input: String,
    val output: String
) {
    constructor(name: String, input: String, output: String) : this(
        0,
        name,
        input,
        output
    ) // constructor for creating new instances to be added to db
}