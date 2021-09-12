package com.github.pushpavel.autocp.tester.errors

sealed class ProcessRunnerErr(message: String?) : Exception(message) {
    object DeadProcessErr : ProcessRunnerErr(null)
    class RuntimeErr(val output: String, message: String?) : ProcessRunnerErr(message)
    class TimeoutErr(val timeLimit: Long) : ProcessRunnerErr(null)
}