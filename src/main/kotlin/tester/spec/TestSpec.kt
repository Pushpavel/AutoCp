package tester.spec

import files.TestcaseSpec

class TestSpec(
    name: String,
    val input: String,
    val expectedOutput: String
) : BaseSpec(name, null) {
    companion object {
        fun fromTestCase(testcase: TestcaseSpec) = TestSpec(
            testcase.getName(),
            testcase.input,
            testcase.output
        )
    }
}