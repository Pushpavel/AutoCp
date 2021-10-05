package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.errors.NoReachErr

object FileGeneratorProvider {
    private val registeredFileGenerators = mutableListOf<FileGenerator>(DefaultFileGenerator())

    fun registerFileGenerator(g: FileGenerator) = registeredFileGenerators.add(0, g)

    fun getSupportedFileGenerator(extension: String): FileGenerator {
        for (g in registeredFileGenerators)
            if (g.isSupported(extension))
                return g

        throw NoReachErr
    }
}