package config.validators

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import database.autoCp
import database.models.SolutionFile
import settings.langSettings.AutoCpLangSettings
import kotlin.io.path.Path
import kotlin.io.path.pathString

fun getValidSolutionFile(project: Project, configName: String, path: String): SolutionFile {
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

    val db = project.autoCp()

    if (!db.solutionFiles.containsKey(pathString))
        throw SolutionFilePathErr.FileNotRegistered(configName, pathString)

    val solutionFile = db.solutionFiles[pathString]!!

    AutoCpLangSettings.findLangByFile(file) ?: throw SolutionFilePathErr.LangNotRegistered(configName, solutionFile)

    return solutionFile
}