package tester.spec

import common.TestCase

class TestSpec(
    name: String,
    val input: String,
    val expectedOutput: String
) : BaseSpec(name, null) {
    companion object {
        fun fromTestCase(testcase: TestCase) = TestSpec(
            testcase.getName(),
            testcase.input,
            testcase.output
        )
    }
}