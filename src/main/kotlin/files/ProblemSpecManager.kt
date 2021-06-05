package files

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import common.Constants.SPEC_EXTENSION
import common.Constants.SPEC_FOLDER
import java.io.File
import java.io.FileFilter
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ProblemSpecManager(private val project: Project) {

    private val gson by lazy { Gson() }

    fun findSpec(solutionFilePath: String): ProblemSpec? {
        val solutionFile = File(solutionFilePath)

        if (!solutionFile.exists())
            throw IllegalArgumentException("Solution File at $solutionFilePath Does not exists")

        val specDir = Paths.get(project.basePath!!, SPEC_FOLDER).toFile()
        val files = specDir.listFiles(FileFilter { it.extension == SPEC_EXTENSION }) ?: emptyArray()

        for (file in files) {
            if (file.exists()) {
                val jsonString = file.readText()
                val problem = gson.fromJson(jsonString, ProblemSpec::class.java)
                problem.file = file
                val solutionPaths = problem.solutionFiles.map { Path.of(it) }

                if (solutionPaths.any { Path.of(solutionFilePath).equals(it) })
                    return problem
            }
        }

        return null
    }

    fun createSpec(spec: ProblemSpec) {
        val specDir = Paths.get(project.basePath!!, SPEC_FOLDER).toFile()

        if (!specDir.exists())
            specDir.mkdir()

        val json = gson.toJson(spec)

        val path = Paths.get(specDir.path, "${spec.name}.${json.hashCode()}.$SPEC_EXTENSION")
        val file = path.toFile()
        file.createNewFile()

        val solutionFilePath = createSolutionFile(spec)
        spec.solutionFiles.add(solutionFilePath)
        file.writeText(gson.toJson(spec))
    }

    // todo: migrate to more appropriate place
    // todo: support different extensions
    private fun createSolutionFile(spec: ProblemSpec): String {
        val rootDir = Paths.get(project.basePath!!, spec.group).toFile()
        if (!rootDir.exists())
            rootDir.mkdir()

        val path = Paths.get(rootDir.path, "${spec.name}.cpp")
        val file = path.toFile()
        file.createNewFile()
        return file.path
    }

}