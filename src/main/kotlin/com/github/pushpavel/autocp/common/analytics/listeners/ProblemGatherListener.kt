package com.github.pushpavel.autocp.common.analytics.listeners

import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.BatchGatherEvent
import com.github.pushpavel.autocp.core.persistance.storables.problems.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.vfs.VirtualFile

class ProblemGatherListener : FileGenerationListener {
    var currentBatch: BatchJson? = null
    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
        if (currentBatch != batch) {
            GoogleAnalytics.instance.sendEvent(BatchGatherEvent(problem.groupName, extension, batch.size))
            currentBatch = batch
        }
    }
}