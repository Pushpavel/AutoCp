package com.github.pushpavel.autocp.tester.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UtilsKtTest {

    @Test
    fun splitCommandString() {
        val command =
            """g++ "C\data\CLionProjects\CodeChef - Summer of Innovation\Moodle Chat.cpp" -o "C:\Temp\AutoCp-515\AutoCp-Blow weed everyday-874.exe""""
        val expectedCommandList = listOf(
            "g++",
            """C\data\CLionProjects\CodeChef - Summer of Innovation\Moodle Chat.cpp""",
            "-o",
            """C:\Temp\AutoCp-515\AutoCp-Blow weed everyday-874.exe"""
        )
        val commandList = splitCommandString(command)
        assertEquals(expectedCommandList, commandList)
    }
}