package tester.spec

import files.ProblemSpec
import tester.run.ProgramExecutorFactory

class TestGroupSpec(
    name: String,
    val testSpecs: List<TestSpec>,
    val testGroupSpecs: List<TestGroupSpec>,
    programFactory: ProgramExecutorFactory?
) : BaseSpec(name, programFactory) {

    init {
        for (spec in testSpecs)
            spec.setParent(this)
        for (spec in testGroupSpecs)
            spec.setParent(this)
    }

    companion object {

        fun fromProblem(problem: ProblemSpec, executablePath: String): TestGroupSpec {

            val programFactory = ProgramExecutorFactory(executablePath)

            return TestGroupSpec(
                problem.name,
                problem.testcases.map { TestSpec.fromTestCase(it) },
                emptyList(),
                programFactory
            )
        }
    }
}