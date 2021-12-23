package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.core.persistance.storables.problems.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.vfs.VirtualFile

interface FileGenerator {
    fun isSupported(extension: String): Boolean

    fun generateFile(extension: String, problem: Problem, batch: BatchJson): VirtualFile?
}