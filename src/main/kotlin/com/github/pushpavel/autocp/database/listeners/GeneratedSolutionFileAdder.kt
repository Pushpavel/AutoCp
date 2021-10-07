package com.github.pushpavel.autocp.database.listeners

import com.github.pushpavel.autocp.database.autoCp
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class GeneratedSolutionFileAdder(val project: Project) : FileGenerationListener {
    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
        addSolutionFile(file.path, problem)
    }

    override fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {
        if (e is GenerateFileErr.FileAlreadyExistsErr)
            addSolutionFile(e.filePath, problem)
    }

    private fun addSolutionFile(filePath: String, problem: Problem) {
        project.autoCp().addSolutionFile(filePath, Pair(problem.groupName, problem.name))
    }
}