package com.github.pushpavel.autocp.database.listeners

import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.core.persistance.storables.problems.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import kotlin.io.path.Path

class GeneratedSolutionFileAdder(val project: Project) : FileGenerationListener {
    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
        addSolutionFile(file.path, problem)
    }

    override fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {
        if (e is GenerateFileErr.FileAlreadyExistsErr)
            addSolutionFile(e.filePath, problem)
    }

    private fun addSolutionFile(absPath: String, problem: Problem) {
        SolutionFiles.getInstance(project).upsertFile(Path(absPath), problem)
    }
}