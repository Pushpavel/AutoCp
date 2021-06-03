package tester

import com.intellij.openapi.util.Disposer
import tester.process.ProcessLike
import tester.result.TestListener
import tester.spec.TestGroupSpec

class TestGroupExecutor(private val spec: TestGroupSpec, private val listener: TestListener) : ProcessLike {

    override fun start() {
        listener.testGroupStarted(spec)

        // run tests
        for (testSpec in spec.testSpecs) {
            val childTest = TestExecutor(testSpec, listener)
            childTest.start()
            Disposer.register(this, childTest)
        }

        // run testGroups
        for (groupSpec in spec.testGroupSpecs) {
            val childGroup = TestGroupExecutor(spec, listener)
            childGroup.start()
            Disposer.register(this, childGroup)
        }

        listener.testGroupFinished(spec)
    }

    override fun dispose() {

    }
}