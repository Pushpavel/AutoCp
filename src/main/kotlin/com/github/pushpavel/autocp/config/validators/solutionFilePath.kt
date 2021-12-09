package com.github.pushpavel.autocp.config.validators

import com.github.pushpavel.autocp.core.persistance.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.solutions.Solutions
import com.github.pushpavel.autocp.core.persistance.storage.storable
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import kotlin.io.path.Path
import kotlin.io.path.pathString

// todo: rename to getValidSolution
fun getValidSolutionFile(project: Project, configName: String, path: String): Solution {
    if (path.isBlank())
        throw SolutionFilePathErr.BlankPathErr(configName)

    val file: VirtualFile?
    val pathString: String
    try {
        val p = Path(path)
        pathString = p.pathString
        file = LocalFileSystem.getInstance().findFileByNioFile(p)
    } catch (e: Exception) {
        throw SolutionFilePathErr.FormatErr(configName, path)
    }

    if (file?.exists() != true)
        throw SolutionFilePathErr.FileDoesNotExist(configName, pathString)

    val solutions = project.storable<Solutions>()

    if (pathString !in solutions)
        throw SolutionFilePathErr.FileNotRegistered(configName, pathString)

    return solutions[pathString]!!
}