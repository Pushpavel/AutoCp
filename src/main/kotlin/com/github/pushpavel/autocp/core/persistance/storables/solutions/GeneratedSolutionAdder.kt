package com.github.pushpavel.autocp.core.persistance.storables.solutions

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.core.persistance.storable
import com.github.pushpavel.autocp.core.persistance.storables.problems.Problem
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcases
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class GeneratedSolutionAdder(val project: Project) : FileGenerationListener {
    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
        addSolutionFile(file.pathString, problem)
    }

    override fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {
        if (e is GenerateFileErr.FileAlreadyExistsErr)
            addSolutionFile(e.filePath, problem)
    }

    private fun addSolutionFile(absPath: String, problem: Problem) {
        val solutions = project.storable<Solutions>()
        val solution = Solution(
            problem.name,
            absPath,
            timeLimit = problem.timeLimit.toInt(),
            groupName = problem.groupName
        )
        solutions.put(solution)

        val testcases = project.storable<Testcases>()
        val model = testcases.getOrPut(absPath)
        model.removeAll()
        model.addAll(0, problem.sampleTestcases)
    }
}