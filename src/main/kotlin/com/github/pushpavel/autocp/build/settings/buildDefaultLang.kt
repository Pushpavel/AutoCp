package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.DefaultLangData
import com.github.pushpavel.autocp.build.Lang
import java.io.File


fun buildDefaultLangs(configs: List<DefaultLangData>): Map<String, Lang> {
    val pathExes = buildPathExes()
    return configs.map {
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