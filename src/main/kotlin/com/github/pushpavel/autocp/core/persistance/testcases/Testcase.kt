package com.github.pushpavel.autocp.core.persistance.testcases

data class Testcase(
    val pathString: String,
    val num: Int,
    val input: String,
    val output: String,
) {
    data class Key(val pathString: String, val num: Int)

    fun getKey(): Key = Key(pathString, num)
}