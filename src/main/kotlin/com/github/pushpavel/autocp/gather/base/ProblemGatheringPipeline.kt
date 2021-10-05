package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.project.Project

/**
 * Handles [showProblemGatheringDialog] , File Generation Delegation and clearing current Batch
 */
class ProblemGatheringPipeline(project: Project) : ProblemGatheringListener {

    override fun onBatchStart(problem: Problem, batch: BatchJson) {

    }
}