package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.core.persistance.storables.problems.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.Topic

interface FileGenerationListener {
    companion object {
        val TOPIC = Topic.create("Solution File Generation", FileGenerationListener::class.java)
    }

    fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {}

    fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {}
}