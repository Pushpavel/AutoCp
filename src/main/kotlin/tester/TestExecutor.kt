package tester

import com.intellij.openapi.util.Disposer
import tester.diff.DiffMarker
import tester.process.ProcessLike
import tester.result.ProgramResult
import tester.result.TestListener
import tester.spec.TestSpec

class TestExecutor(private val spec: TestSpec, private val listener: TestListener) : ProcessLike {
    override fun start() {
        listener.testStarted(spec)

        val executor = spec.getProgramFactory().createExecutor(spec.input)

        Disposer.register(this, executor)

        val result = executor.execute()

        processResult(result)

        listener.testFinished(spec)
    }

    private fun processResult(result: ProgramResult) {
        if (result.error.isNotEmpty()) {
            if (result.output.isNotEmpty())
                listener.testOutput(spec, result.output)
            listener.testError(spec, result.error)
            listener.testFailed(spec, "Program Crashed")
            return
        }
        val marker = DiffMarker(result.output, spec.expectedOutput)

        if (marker.segments.size > 1 || (marker.segments.size == 1 && marker.segments[0].markType == DiffMarker.DiffType.DIFFERENT)) {
            listener.testFailed(spec, "Not Expected Output")
            listener.testOutput(spec, "Your Solution Output:\n")

            marker.forEachString1Segment { segment, type ->
                when (type) {
                    DiffMarker.DiffType.IDENTICAL -> listener.testOutput(spec, segment)
                    DiffMarker.DiffType.DIFFERENT -> listener.testError(spec, segment)
                }
            }

            listener.testOutput(spec, "\nExpected Output:\n")

            marker.forEachString2Segment { segment, type ->
                when (type) {
                    DiffMarker.DiffType.IDENTICAL -> listener.testOutput(spec, segment)
                    DiffMarker.DiffType.DIFFERENT -> listener.testError(spec, segment)
                }
            }

        } else
            listener.testOutput(spec, result.output)
    }

    override fun dispose() {
    }
}