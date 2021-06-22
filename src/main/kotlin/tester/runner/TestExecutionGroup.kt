package tester.runner

import com.intellij.openapi.util.Disposer
import tester.execute.ProcessLike
import tester.models.TestGroupSpec

class TestExecutionGroup(private val spec: TestGroupSpec, private val listener: TestListener) : ProcessLike {

    override fun start() {
        listener.testGroupStarted(spec)

        // run tests
        for (testSpec in spec.testSpecs) {
            val childTest = TestExecution(testSpec, listener)
            childTest.start()
            Disposer.register(this, childTest)
        }

        // run testGroups
        for (groupSpec in spec.testGroupSpecs) {
            val childGroup = TestExecutionGroup(spec, listener)
            childGroup.start()
            Disposer.register(this, childGroup)
        }

        listener.testGroupFinished(spec)
    }

    override fun dispose() {

    }
}