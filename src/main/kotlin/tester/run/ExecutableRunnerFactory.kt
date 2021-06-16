package tester.run

class ExecutableRunnerFactory(private val executionPath: String) {

    fun createExecutor(input: String): ExecutableRunner {
        return ExecutableRunner(executionPath, input)
    }
}