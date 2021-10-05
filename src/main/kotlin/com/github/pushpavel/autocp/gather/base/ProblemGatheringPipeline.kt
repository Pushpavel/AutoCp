package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerator
import com.github.pushpavel.autocp.gather.filegen.FileGeneratorProvider
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.Queue

/**
 * Handles [showProblemGatheringDialog] , File Generation Delegation and clearing current Batch
 */
class ProblemGatheringPipeline(val project: Project) : ProblemGatheringListener {

    private var flush = false
    private var fileGenerator: FileGenerator? = null
    private val problemQueue = Queue<Problem>()

    override fun onBatchStart(problem: Problem, batch: BatchJson) {
        val confirm = showProblemGatheringDialog(project, problem.groupName)
        if (!confirm) {
            BatchProcessor.interruptBatch(ProblemGatheringErr.Cancellation)
            return
        }

        fileGenerator = FileGeneratorProvider.getSupportedFileGenerator(project.autoCpProject().defaultFileExtension)

        flushProblemQueue(batch)
        if (problemQueue.size == batch.size) {
            BatchProcessor.interruptBatch()
            flush = false
            fileGenerator = null
        }
    }

    override fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {
        problemQueue.add(problems.last())
        if (!flush) return
        flushProblemQueue(batch)
        if (problems.size == batch.size) {
            BatchProcessor.interruptBatch()
            flush = false
            fileGenerator = null
        }
    }

    private fun flushProblemQueue(batch: BatchJson) {
        flush = true
        while (problemQueue.isNotEmpty()) {
            fileGenerator?.generateFile(project.autoCpProject().defaultFileExtension, problemQueue.remove(), batch)
        }
    }
}