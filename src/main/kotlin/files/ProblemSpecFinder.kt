package files

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import common.Constants.SPEC_EXTENSION
import common.Constants.SPEC_FOLDER
import common.ProblemJson
import org.jetbrains.annotations.Nullable
import java.io.File
import java.io.FileFilter
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ProblemSpecFinder(private val project: Project) {

    private val gson by lazy { Gson() }

    fun find(solutionFilePath: String): ProblemSpec? {
        val solutionFile = File(solutionFilePath)

        if (!solutionFile.exists())
            throw IllegalArgumentException("Solution File at $solutionFilePath Does not exists")

        val specDir = Paths.get(project.basePath!!, SPEC_FOLDER).toFile()
        val files = specDir.listFiles(FileFilter { it.extension == SPEC_EXTENSION }) ?: emptyArray();

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

}