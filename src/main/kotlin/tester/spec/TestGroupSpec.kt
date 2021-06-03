package tester.spec

import common.AutoCpProblem

data class TestGroupSpec(
    val name: String,
    val testSpecs: List<TestSpec>,
    val testGroupSpecs: List<TestGroupSpec>
) {
    companion object {
        fun fromProblem(problem: AutoCpProblem) = TestGroupSpec(
            problem.name,
            problem.tests.map { TestSpec.fromTestCase(it) },
            emptyList(),
        )
    }
}