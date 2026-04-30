package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.IdeFocusManager

/**
 * Manages Batch of problems and notifies [ProblemGatheringListener]
 */
object BatchProcessor {
    private var currentBatch: BatchJson? = null
    private val ignoredBatches = mutableSetOf<BatchJson>()
    private val parsedProblems = mutableListOf<Problem>()

    // Frozen for the lifetime of currentBatch so events stay on a single project
    // even if user switches focus mid-batch.
    private var batchTargetProject: Project? = null

    private fun resolveTargetProject(): Project? {
        if (!AutoCpGeneralSettings.instance.onlyActiveWindow) return null

        val focused = IdeFocusManager.getGlobalInstance().lastFocusedFrame?.project
            ?.takeIf { !it.isDisposed }
        if (focused != null) return focused

        return ProjectManager.getInstance().openProjects.firstOrNull { !it.isDisposed }
    }

    private fun batchPublisher(): ProblemGatheringListener {
        val target = batchTargetProject?.takeIf { !it.isDisposed }
        return if (target != null)
            target.messageBus.syncPublisher(ProblemGatheringListener.TOPIC)
        else
            ApplicationManager.getApplication().messageBus.syncPublisher(ProblemGatheringListener.TOPIC)
    }

    fun onJsonReceived(json: ProblemJson) {
        val problem = json.toProblem()
        val batch = json.batch

        // ignoring batches which is not currentBatch or already ignored
        if (ignoredBatches.contains(batch) || (currentBatch != null && currentBatch != batch)) {
            ignoredBatches.add(batch)
            return
        }

        parsedProblems.add(problem)

        if (currentBatch == null) {
            batchTargetProject = resolveTargetProject()
            batchPublisher().onBatchStart(problem, batch)
        }

        currentBatch = batch
        batchPublisher().onProblemGathered(parsedProblems.toList(), batch)
    }

    fun interruptBatch(e: Exception? = null) {
        if (currentBatch != null)
            batchPublisher().onBatchEnd(e, parsedProblems.toList(), currentBatch!!)

        currentBatch?.let { ignoredBatches.add(it) }
        parsedProblems.clear()
        currentBatch = null
        batchTargetProject = null
    }

    fun isCurrentBatchBlocking() = currentBatch != null && currentBatch?.size == parsedProblems.size
}
