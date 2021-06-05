package plugin.services

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import common.AutoCpProblem
import java.io.File
import java.nio.file.Paths

@Deprecated("use ProblemSpecManager")
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
}