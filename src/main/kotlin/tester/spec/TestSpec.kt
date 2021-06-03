package tester.spec

import common.TestCase

data class TestSpec(val name: String) {
    companion object {
        fun fromTestCase(testcase: TestCase) = TestSpec(testcase.getName())
    }
}