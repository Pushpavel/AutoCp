package tester.utils

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