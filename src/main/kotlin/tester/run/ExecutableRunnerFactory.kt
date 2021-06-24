package tester.run

@Deprecated("use SolutionProcessFactory")
class ExecutableRunnerFactory(private val executionPath: String) {

    fun createExecutor(input: String): ExecutableRunner {
        return ExecutableRunner(executionPath, input)
    }
}