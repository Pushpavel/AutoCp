package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.FileGenerationDto
import com.intellij.openapi.vfs.VirtualFile

interface FileGenerator {
    fun isSupported(extension: String): Boolean

    fun generateFile(extension: String, dto: FileGenerationDto, problem: Problem, batch: BatchJson): VirtualFile?
}