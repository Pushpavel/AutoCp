package com.github.pushpavel.autocp.tester.errors

sealed class TestcaseJudgingErr(message: String?) : Exception(message) {

    class JudgeFailed(val output: String, message: String? = null, val exitCode: Int?) : TestcaseJudgingErr(message)

}
