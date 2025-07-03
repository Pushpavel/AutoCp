package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.database.models.Problem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class CppRustFileGenerator(project: Project) : DefaultFileGenerator(project) {
    override fun isSupported(extension: String): Boolean = extension == "cpp" || extension == "rs"
    override fun getFileNameWithExtension(parentPsiDir: PsiDirectory, fileName: String, extension: String): String {
        val newFileName = defaultConversion(fileName)
        return "${newFileName}.$extension"
    }
}