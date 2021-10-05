package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.DefaultLangData
import com.github.pushpavel.autocp.build.Lang
import com.intellij.execution.Platform
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.UnknownFileType
import java.io.File


fun buildDefaultLangs(configs: List<DefaultLangData>): Map<String, Lang> {
    val pathExes = buildPathExes()
    return configs.filter {
        FileTypeManager.getInstance().getFileTypeByExtension(it.extension) !is UnknownFileType
    }.map {
        // making default commands platform independent
        it.copy(
            commands = it.commands.map { p ->
                p.copy(
                    p.first?.let { it1 -> processCommand(it1) },
                    processCommand(p.second)
                )
            }
        )
    }.map {
        val commandPair = it.commands.firstOrNull { p ->
            val command = p.first ?: return@firstOrNull true
            val index = command.indexOfAny(" .".toCharArray())
            val exe = command.substring(0, index)
            pathExes.contains(exe)
        } ?: it.commands[0]

        return@map Lang(
            it.extension,
            commandPair.first,
            commandPair.second,
            it.lineCommentPrefix,
            true
        )
    }.associateBy { it.extension }
}


fun buildPathExes(): MutableSet<String> {
    val set = mutableSetOf<String>()
    val path = System.getenv("PATH")
    for (p in path.split(';')) {
        val dir = File(p)
        dir.listFiles()?.forEach { set.add(it.nameWithoutExtension) }
    }

    return set
}


private fun processCommand(command: String): String {
    var c = command
    for (k in getPlatformMacros())
        c = c.replace(k.key, k.value)
    return c
}

private fun getPlatformMacros(): Map<String, String> {
    return when (Platform.current()) {
        Platform.WINDOWS -> mapOf(
            "/" to "\\",
        )
        Platform.UNIX -> mapOf(
            ".exe" to "",
            "\\" to "/"
        )
    }
}