package com.github.pushpavel.autocp.gather

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.database.models.Problem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.Topic

interface FileGenerationListener {
    companion object {
        val TOPIC = Topic.create("Solution File Generation", FileGenerationListener::class.java)
    }

    fun onGenerated(file: VirtualFile, problem: Problem, lang: Lang, alreadyGenerated: Boolean)
}