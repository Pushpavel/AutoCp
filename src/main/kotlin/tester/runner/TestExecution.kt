package tester.runner

import com.intellij.openapi.util.Disposer
import tester.execute.ProcessLike
import tester.models.ProgramResult
import tester.models.ResultCode
import tester.models.TestSpec

class TestExecution(private val spec: TestSpec, private val listener: TestListener) : ProcessLike {
    override fun start() {
        listener.testStarted(spec)

        val executor = spec.getProgramFactory().createExecutor(spec.input)

        Disposer.register(this, executor)


        val result = try {
            executor.execute()
        } catch (e: Exception) {
            ProgramResult("", e.localizedMessage)
        }

        processResult(result)

        listener.testFinished(spec)
    }

    private fun processResult(result: ProgramResult) {
        listener.testOutput(spec, result.output)

        if (result.error.isNotEmpty()) {
            listener.testError(spec, result.error)
            listener.testFailed(spec, ResultCode.PROGRAM_CRASH)
            return
        }

        val expectedOutput = spec.expectedOutput.trim()
        val actualOutput = result.output.trim().replace("\r", "")

        if (!actualOutput.contentEquals(expectedOutput)) {
            listener.testError(spec, "\n\nExpected Output:\n$expectedOutput")
            listener.testFailed(spec, ResultCode.WRONG_ANSWER)
        } else
            listener.testPassed(spec)
    }

    override fun dispose() {
    }
}