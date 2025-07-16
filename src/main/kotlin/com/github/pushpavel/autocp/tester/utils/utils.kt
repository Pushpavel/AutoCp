package com.github.pushpavel.autocp.tester.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * splits single command string into the program and its arguments strings
 */
fun splitCommandString(command: String): List<String> {
    val commandList = Regex(""""(\\"|[^"])*?"|[^\s]+""").findAll(command).toList()
    return commandList.map { it.value.trim('"') }
}

fun String.trimByLines(): String {
    return this
        .trim()
        .replace("\r", "")
        .split('\n')
        .joinToString("\n")
        { it.trim() }
}

suspend fun createFile(directory: File, fileName: String, content: String): File {
    val file = File(directory, fileName)
    withContext(Dispatchers.IO) {
        if (file.exists())
            file.delete()
        file.createNewFile()
        file.writeText(content, Charsets.UTF_8)
    }
    return file
}

suspend fun readFile(directory: File, fileName: String): String? {
    val file = File(directory, fileName)
    return withContext(Dispatchers.IO) {
        if (file.exists())
            file.readText(Charsets.UTF_8)
        else
            null
    }
}