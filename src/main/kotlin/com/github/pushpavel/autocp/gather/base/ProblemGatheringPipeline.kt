package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.errors.NoReachErr
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.filegen.FileGenerator
import com.github.pushpavel.autocp.gather.filegen.FileGeneratorProvider
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.github.pushpavel.autocp.gather.models.FileGenerationDto
import com.github.pushpavel.autocp.gather.models.GenerateFileErr
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.generalSettings.OpenFileOnGather
import com.github.pushpavel.autocp.settings.projectSettings.autoCpProject
import com.intellij.ide.actions.OpenFileAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.jetbrains.rd.util.Queue
import kotlin.io.path.Path

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
    private var dtos: MutableList<FileGenerationDto>? = null;
    private val subscriber by lazy { project.messageBus.syncPublisher(FileGenerationListener.TOPIC) }

    override fun onBatchStart(problem: Problem, batch: BatchJson) {
    }

    override fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {
        problemQueue.add(problems.last())
        if (problems.size == batch.size) {
            BatchProcessor.interruptBatch()
            flush = false
            fileGenerator = null
        }
    }

    override fun onBatchEnd(e: Exception?, problems: List<Problem>, batch: BatchJson) {
        if (problems.isEmpty())
            return;
        runInEdt {
            log.debug("Showing Problem Gathering dialog...", batch, problems)
            dtos = showProblemGatheringDialog(project, problems)?.toMutableList()
            if (dtos == null) {
                log.debug("Cancelling batch", batch, problems)
                problemQueue.clear()
                BatchProcessor.interruptBatch(ProblemGatheringErr.Cancellation)
                return@runInEdt
            }
            fileGenerator =
                fileGeneratorProvider.getSupportedFileGenerator(project.autoCpProject().defaultFileExtension)

            val preComplete = problemQueue.size == batch.size

            flushProblemQueue(batch)
        }
    }

    private fun flushProblemQueue(batch: BatchJson) {
        if (dtos == null)
            return
        val extension = project.autoCpProject().defaultFileExtension
        flush = true
        while (problemQueue.isNotEmpty()) {
            val problem = problemQueue.remove()
            val dto = dtos!!.removeAt(0);
            val openFile = when (AutoCpGeneralSettings.instance.openFilesOnGather) {
                OpenFileOnGather.NONE -> false
                OpenFileOnGather.ONLY_FIRST -> currentBatch != batch
                OpenFileOnGather.ALL -> true
            }

            currentBatch = batch
            val fileGen = fileGenerator ?: throw NoReachErr

            DumbService.getInstance(project).smartInvokeLater({
                try {
                    val file = fileGen.generateFile(extension, dto, problem, batch)
                    ProjectView.getInstance(project).refresh()

                    if (file != null)
                        subscriber.onGenerated(file, problem, batch, extension)

                    if (openFile && file != null)
                        OpenFileAction.openFile(file, project)


                } catch (e: Exception) {
                    // opens file even if it was generated already
                    if (openFile && e is GenerateFileErr.FileAlreadyExistsErr) {
                        val file = VfsUtil.findFile(Path(e.filePath), true)
                        if (file != null)
                            OpenFileAction.openFile(file, project)
                    }

                    e.printStackTrace()
                    subscriber.onError(e, problem, batch, extension)
                }
            }, ModalityState.NON_MODAL)
        }

    }
}
