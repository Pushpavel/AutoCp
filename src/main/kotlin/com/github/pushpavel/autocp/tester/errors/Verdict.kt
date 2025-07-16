package com.github.pushpavel.autocp.tester.errors

import com.github.pushpavel.autocp.database.models.Testcase

sealed interface Verdict {

    class CorrectAnswer(val output: String, val executionTime: Long, val comment: String? = null) : Verdict
    class WrongAnswer(val expectedOutput: String?, val actualOutput: String, val executionTime: Long, val comment: String? = null) : Verdict
    class RuntimeErr(val output: String, val errMsg: String) : Verdict
    class InternalErr(val err: Exception) : Verdict
    class TimeLimitErr(val timeLimit: Long) : Verdict
}