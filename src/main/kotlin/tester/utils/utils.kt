package tester.utils

fun splitCommandString(command: String): List<String> {
    val commandList = Regex(""""(\\"|[^"])*?"|[^\s]+""").findAll(command).toList()
    return commandList.map { it.value.trim('"') }
}
