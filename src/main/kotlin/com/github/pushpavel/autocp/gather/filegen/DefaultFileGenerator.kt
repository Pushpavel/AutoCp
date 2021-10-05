package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.problems.Problem

class DefaultFileGenerator : FileGenerator {
    override fun isSupported(lang: String) = true

    override fun generateFile(lang: Lang, problem: Problem, batch: BatchJson): VirtualFile? {
        TODO("Not yet implemented")
    }
}