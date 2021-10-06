package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.errors.NoReachErr
import com.intellij.openapi.project.Project

class FileGeneratorProvider(project: Project) {
    private val registeredFileGenerators = listOf<FileGenerator>(
        DefaultFileGenerator(project)
    )

    fun getSupportedFileGenerator(extension: String): FileGenerator {
        for (g in registeredFileGenerators)
            if (g.isSupported(extension))
                return g

        throw NoReachErr
    }
}