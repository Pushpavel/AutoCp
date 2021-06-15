package database.models


@Deprecated("after migration use Testcase")
data class TestcaseSpec(
    @Deprecated("use problemName & problemGroup")
    val id: Int,
    val name: String,
    val input: String,
    val output: String,
    val problemName: String,
    val problemGroup: String,
) {
    @Deprecated("This constructor should be removed")
    constructor(name: String, input: String, output: String) : this(
        0,
        name,
        input,
        output,
        "",
        ""
    ) // constructor for creating new instances to be added to db

}