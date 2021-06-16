package tester.spec

import database.models.Testcase
import files.TestcaseSpec

class TestSpec(
    name: String,
    val input: String,
    val expectedOutput: String
) : BaseSpec(name, null) {
    companion object {
        @Deprecated("",
            ReplaceWith("TestSpec(testcase.getName(), testcase.input, testcase.output)", "tester.spec.TestSpec")
        )
        fun fromTestCase(testcase: TestcaseSpec) = TestSpec(
            testcase.getName(),
            testcase.input,
            testcase.output
        )

        fun fromTestcase(testcase: Testcase) = TestSpec(
            testcase.name,
            testcase.input,
            testcase.output
        )
    }
}