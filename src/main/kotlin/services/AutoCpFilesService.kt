package services

import com.google.common.io.Files
import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import common.AutoCpProblem
import java.io.File
import java.nio.file.Paths

@Service
class AutoCpFilesService(private val project: Project) {

    private val gson: Gson by lazy { Gson() }

    fun getAutoCp(fileName: String): AutoCpProblem? {
        val file = getAutoCpFile(fileName) ?: return null
        return gson.fromJson(file.reader(), AutoCpProblem::class.java)
    }

    fun getAutoCpFile(fileName: String): File? {
        val path = project.basePath?.let {
            return@let Paths.get(it, ".autocp", "$fileName.autocp")
        } ?: return null


        val file = path.toFile()

        if (!file.exists()) return null
        return file
    }

    fun createAutoCpFile(extension: String, problem: AutoCpProblem): Boolean {
        val basePath = project.basePath ?: return false
        val path = Paths.get(basePath, ".autocp", "${problem.name}.autocp")
        val solutionPath = Paths.get(basePath, problem.group, "${problem.name}.$extension")


        val file = path.toFile()
        Files.createParentDirs(file)
        file.createNewFile()
        file.writeText(gson.toJson(problem))

        val solutionFile = solutionPath.toFile()
        Files.createParentDirs(solutionFile)
        solutionFile.createNewFile()
        return true
    }
}