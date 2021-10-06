package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerator
import com.github.pushpavel.autocp.gather.filegen.FileGeneratorProvider
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.generalSettings.OpenFileOnGather
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.ide.actions.OpenFileAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.Queue

/**
 * Handles [showProblemGatheringDialog] , File Generation Delegation and clearing current Batch
 */
class ProblemGatheringPipeline(val project: Project) : ProblemGatheringListener {

    private val log = Logger.getInstance(ProblemGatheringPipeline::class.java)

    private var flush = false
    private var fileGenerator: FileGenerator? = null
    private val problemQueue = Queue<Problem>()
    private val fileGeneratorProvider = FileGeneratorProvider(project)
    private var currentBatch: BatchJson? = null

    override fun onBatchStart(problem: Problem, batch: BatchJson) {
        runInEdt {
            log.debug("Showing Problem Gathering dialog...", batch, problem)
            val confirm = showProblemGatheringDialog(project, problem.groupName)
            if (!confirm) {
                log.debug("Cancelling batch", batch, problem)
                BatchProcessor.interruptBatch(ProblemGatheringErr.Cancellation)
                return@runInEdt
            }
            fileGenerator =
                fileGeneratorProvider.getSupportedFileGenerator(project.autoCpProject().defaultFileExtension)

            flushProblemQueue(batch)
            if (problemQueue.size == batch.size) {
                BatchProcessor.interruptBatch()
                flush = false
                fileGenerator = null
            }
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

        invokeLater(ModalityState.NON_MODAL) {
            flush = true
            while (problemQueue.isNotEmpty()) {
                val openFile = when (AutoCpGeneralSettings.instance.openFilesOnGather) {
                    OpenFileOnGather.NONE -> false
                    OpenFileOnGather.ONLY_FIRST -> currentBatch != batch
                    OpenFileOnGather.ALL -> true
                }
                currentBatch = batch
                val file = fileGenerator?.generateFile(
                    project.autoCpProject().defaultFileExtension,
                    problemQueue.remove(),
                    batch
                )
                ProjectView.getInstance(project).refresh()

                if (openFile && file != null)
                    OpenFileAction.openFile(file, project)
            }
        }

    }
}
