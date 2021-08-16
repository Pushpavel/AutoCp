package tester.errors

sealed interface Verdict {
    class CorrectAnswer(val output: String, val executionTime: Long) : Verdict
    class WrongAnswer(val expectedOutput: String, val actualOutput: String, val executionTime: Long) : Verdict
    class RuntimeErr(val output: String, val errMsg: String) : Verdict
    class InternalErr(val err: Exception) : Verdict
    class TimeLimitErr(val timeLimit: Long) : Verdict
}