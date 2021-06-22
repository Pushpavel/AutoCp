package tester.models

import com.github.pushpavel.autocp.database.Problem
import tester.run.ExecutableRunnerFactory

class TestGroupSpec(
    name: String,
    val testSpecs: List<TestSpec>,
    val testGroupSpecs: List<TestGroupSpec>,
    programFactory: ExecutableRunnerFactory?
) : BaseSpec(name, programFactory) {

    init {
        for (spec in testSpecs)
            spec.setParent(this)
        for (spec in testGroupSpecs)
            spec.setParent(this)
    }

    companion object {
        fun fromProblem(problem: Problem, executablePath: String): TestGroupSpec {
            val programFactory = ExecutableRunnerFactory(executablePath)

            return TestGroupSpec(
                problem.name,
                problem.testcases.map { TestSpec.fromTestcase(it) },
                emptyList(),
                programFactory
            )
        }
    }
}