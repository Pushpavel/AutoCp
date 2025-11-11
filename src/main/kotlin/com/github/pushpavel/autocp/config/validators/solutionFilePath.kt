package com.github.pushpavel.autocp.config.validators

import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import kotlin.io.path.Path
import kotlin.io.path.pathString

private val LOG = Logger.getInstance("AutoCpValidators")

fun getValidSolutionFile(project: Project, configName: String, path: String): SolutionFile {
    LOG.warn("AutoCp Debug: getValidSolutionFile called")
    LOG.warn("AutoCp Debug: Config name = $configName")
    LOG.warn("AutoCp Debug: Path = $path")
    if (path.isBlank()) {
        LOG.warn("AutoCp Debug: Path is blank, throwing BlankPathErr")
        throw SolutionFilePathErr.BlankPathErr(configName)
    }
    val file: VirtualFile?
    val pathString: String
    try {
        val p = Path(path)
        pathString = p.pathString
        file = LocalFileSystem.getInstance().findFileByNioFile(p)
        LOG.warn("AutoCp Debug: File resolved: $pathString")
    } catch (e: Exception) {
        LOG.warn("AutoCp Debug: Path format error: ${e.message}")
        throw SolutionFilePathErr.FormatErr(configName, path)
    }

    if (file?.exists() != true) {
        LOG.warn("AutoCp Debug: File does not exist: $pathString")
        throw SolutionFilePathErr.FileDoesNotExist(configName, pathString)
    }
    val solutionFiles = SolutionFiles.getInstance(project)

    if (pathString !in solutionFiles) {
        LOG.warn("AutoCp Debug: File not registered in SolutionFiles: $pathString")
        throw SolutionFilePathErr.FileNotRegistered(configName, pathString)
    }
    LOG.warn("AutoCp Debug: getValidSolutionFile returning successfully")
    return solutionFiles[pathString]!!
}