package tester.run

class ProgramExecutorFactory(private val executionPath: String) {

    fun createExecutor(input: String): ProgramExecutor {
        return ProgramExecutor(executionPath, input)
    }
}