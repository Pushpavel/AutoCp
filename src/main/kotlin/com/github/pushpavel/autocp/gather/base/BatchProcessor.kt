package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.intellij.openapi.application.ApplicationManager

/**
 * Manages Batch of problems and notifies [ProblemGatheringListener]
 */
object BatchProcessor {
    private var currentBatch: BatchJson? = null
    private val ignoredBatches = mutableSetOf<BatchJson>()
    private val parsedProblems = mutableListOf<Problem>()

    private val subscriber by lazy {
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

        if (currentBatch == null)
            subscriber.onBatchStart(problem, batch)

        currentBatch = batch
        subscriber.onProblemGathered(parsedProblems.toList(), batch)
    }

    fun interruptBatch(e: Exception? = null) {
        if (currentBatch != null)
            subscriber.onBatchEnd(e, parsedProblems.toList(), currentBatch!!)

        currentBatch?.let { ignoredBatches.add(it) }
        parsedProblems.clear()
        currentBatch = null
    }
}