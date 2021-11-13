package com.github.pushpavel.autocp.common.compat.base

import com.github.pushpavel.autocp.common.compat.autocpFile.DeleteInvalid
import com.github.pushpavel.autocp.common.compat.autocpFile.RelativizePaths
import com.intellij.openapi.project.Project
import kotlin.io.path.Path
import kotlin.io.path.notExists

class AutoCpFileConversion(val project: Project) {
    private val converters = listOf(
        DeleteInvalid(),
        RelativizePaths(project)
    )

    fun convert() {
        val autoCpFile = Path(project.basePath!!, ".autocp")
        if (autoCpFile.notExists())
            return

        for (converter in converters) {
            if (autoCpFile.notExists())
                return

            converter.convert(autoCpFile)
        }
    }
}