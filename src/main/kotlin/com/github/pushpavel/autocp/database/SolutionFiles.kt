package com.github.pushpavel.autocp.database

import com.github.pushpavel.autocp.common.errors.InternalErr
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.database.models.SolutionFile
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Service
class SolutionFiles(val project: Project) {
    private val db = project.autoCp()
    private val solutionFiles get() = db.solutionFilesFlow.value

    operator fun contains(pathString: String): Boolean {
        val path = relativePath(pathString)
        return solutionFiles.containsKey(path.pathString)
    }

    operator fun get(pathString: String): SolutionFile? {
        val path = relativePath(pathString)
        return solutionFiles[path.pathString]
    }

    fun update(solutionFile: SolutionFile) {
        val path = relativePath(solutionFile.pathString)
        if (path.pathString !in this)
            throw InternalErr("trying to update solution File which does not exist")
        db.modifySolutionFiles { this[path.pathString] = solutionFile.copy(pathString = path.pathString) }
    }

    fun remove(pathString: String) {
        val path = relativePath(pathString)
        db.modifySolutionFiles { remove(path.pathString) }
    }


    fun upsertFile(filePath: Path, problem: Problem? = null) {
        val linkedProblemId = problem?.let { Pair(it.groupName, it.name) }
        upsertFile(filePath, linkedProblemId)
    }

    private fun upsertFile(filePath: Path, linkedProblemId: Pair<String, String>? = null) {
        // relativize the filePath if necessary
        val path = relativePath(filePath.pathString)

        val solutionFile = if (linkedProblemId != null) {
            val problem = db.problems[linkedProblemId.first]?.get(linkedProblemId.second)
                ?: throw InternalErr("trying to create solution File for non existing Problem specification")

            SolutionFile(
                path.pathString,
                linkedProblemId,
                problem.sampleTestcases.toList(),
                problem.timeLimit
            )
        } else
            SolutionFile(
                path.pathString,
                linkedProblemId,
                listOf()
            )

        db.modifySolutionFiles { this[path.pathString] = solutionFile }
    }

    fun listenFlow(pathString: String): Flow<SolutionFile?> {
        val path = relativePath(pathString)
        return db.solutionFilesFlow.map { it[path.pathString] }
    }

    // utils
    private fun relativePath(pathString: String): Path {
        val path = Path(pathString)
        return if (path.isAbsolute) {
            Path(project.basePath!!).relativize(path)
        } else
            path
    }

    companion object {
        fun getInstance(project: Project): SolutionFiles = project.service()
    }
}