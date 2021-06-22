package tester.models

import database.models.Testcase

class TestSpec(
    name: String,
    val input: String,
    val expectedOutput: String
) : BaseSpec(name, null) {
    companion object {

        fun fromTestcase(testcase: Testcase) = TestSpec(
            testcase.name,
            testcase.input,
            testcase.output
        )
    }
}