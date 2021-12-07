package com.github.pushpavel.autocp.gather

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.base.ProblemBatchProcessor
import com.github.pushpavel.autocp.gather.base.ProblemGatheringErr
import com.github.pushpavel.autocp.gather.base.ProblemBatchProcessorListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class ProblemGatheringProgressReporting(
    project: Project
) : Task.Backgroundable(project, "Gathering problems..."), ProblemBatchProcessorListener {

    private var currentIndicator: ProgressIndicator? = null
    private var firstProblem: Problem? = null
    private var currentBatch: BatchJson? = null

    override fun onBatchStart(problem: Problem, batch: BatchJson) {
        firstProblem = problem
        currentBatch = batch

        ProgressManager.getInstance().run(this)
    }

    override fun onBatchEnd(e: Exception?, problems: List<Problem>, batch: BatchJson) {
        currentIndicator = null
        firstProblem = null
        currentBatch = null
    }

    override fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {
        if (currentIndicator != null)
            showProgress(currentIndicator!!, problems, batch.size)
    }

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = false
        if (firstProblem != null && currentBatch != null)
            showProgress(indicator, listOf(firstProblem!!), currentBatch!!.size)
        else
            return

        currentIndicator = indicator

        runBlocking {
            while (!indicator.isCanceled && currentIndicator != null)
                delay(100)
        }

        if (indicator.isCanceled && currentIndicator != null)
            ProblemBatchProcessor.interruptBatch(ProblemGatheringErr.Cancellation)
    }

    private fun showProgress(indicator: ProgressIndicator, problems: List<Problem>, count: Int) {
        indicator.fraction = problems.size.toDouble() / count
        indicator.text = problems.last().name
        indicator.text2 = problems.last().groupName
    }
}