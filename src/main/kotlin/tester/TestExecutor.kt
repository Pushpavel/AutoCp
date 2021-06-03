package tester

import tester.process.ProcessLike
import tester.result.TestListener
import tester.spec.TestSpec

class TestExecutor(private val spec: TestSpec, private val listener: TestListener) : ProcessLike {
    override fun start() {
        listener.testStarted(spec)

        listener.testFinished(spec)
    }

    fun executeTest() {

    }

    override fun dispose() {
    }
}