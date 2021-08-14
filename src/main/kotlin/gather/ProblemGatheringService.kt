package gather

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import common.helpers.*
import common.res.R
import common.res.failed
import database.autoCp
import database.models.Problem
import gather.models.GatheringResult
import gather.models.GenerateFileErr
import gather.models.ServerStatus
import gather.server.ProblemGathering
import gather.server.SimpleLocalServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.generalSettings.AutoCpGeneralSettings
import settings.generalSettings.OpenFileOnGather
import settings.projectSettings.autoCpProject
import java.nio.file.Paths
import kotlin.io.path.pathString

@Service
class ProblemGatheringService(val project: Project) {
    private val scope = ioScope()
    private val db = project.autoCp()

    private val server = SimpleLocalServer(scope, R.others.competitiveCompanionPorts)
    private val gathering = ProblemGathering(scope, server.messages)

    init {
        // setup the pipeline
        ProgressReporter(scope, project, gathering.gathers, gathering::cancelBatch)
    }

    fun startServiceAsync() = server.startAsync()

    fun isRunning() = server.status.value !is ServerStatus.Idle

    fun stopService() = server.stopAsync()


    // Processing the Problems

    init {
        scope.launch { handleGeneratingFiles() }
    }

    private suspend fun handleGeneratingFiles() {
        gathering.gathers.collect {
            catchAndLog {
                if (it is GatheringResult.Gathered) {
                    db.updateProblem(it.problems.last())

                    val openFile = when (AutoCpGeneralSettings.instance.openFilesOnGather) {
                        OpenFileOnGather.NONE -> false
                        OpenFileOnGather.ONLY_FIRST -> it.problems.size == 1
                        OpenFileOnGather.ALL -> true
                    }

                    coroutineScope {
                        launch(Dispatchers.IO) {
                            try {
                                generateFileBlocking(it.problems.last(), openFile)
                            } catch (err: Exception) {
                                if (err !is GenerateFileErr.FileAlreadyExistsErr)
                                    gathering.interruptBatch(err)
                                else {
                                    notifyWarn(
                                        R.strings.fileGenFailedTitle(it.problems.last().name),
                                        R.strings.fileAlreadyExistsMsg(err)
                                    )
                                    // TODO: open file even if it already exists
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun generateFileBlocking(problem: Problem, open: Boolean) {
        val rootPath = Paths.get(project.basePath!!, problem.groupName)
        val rootDir = VfsUtil.createDirectories(rootPath.pathString)
        val rootPsiDir = runReadAction { PsiManager.getInstance(project).findDirectory(rootDir)!! }
        val lang = project.autoCpProject().guessPreferredLang() ?: throw GenerateFileErr.LangNotConfiguredErr(problem)

        val fileTemplate = lang.getFileTemplate() ?: throw GenerateFileErr.FileTemplateMissingErr(lang, problem)

        val fileName = fileTemplate.constructFileNameWithExt(problem.name)

        val filePath = Paths.get(rootPsiDir.virtualFile.path, fileName).pathString

        try {
            rootPsiDir.checkCreateFile(fileName)
        } catch (e: IncorrectOperationException) {
            throw GenerateFileErr.FileAlreadyExistsErr(filePath, problem)
        }

        project.autoCp().createSolutionFile(filePath, Pair(problem.groupName, problem.name))

        invokeLater(ModalityState.NON_MODAL) {
            CreateFileFromTemplateAction.createFileFromTemplate(
                fileName,
                fileTemplate,
                rootPsiDir,
                null,
                open
            )

            ProjectView.getInstance(project).refresh()
        }
    }


    // Notifications

    init {
        scope.launch {
            setupServerNotificationsAsync()
        }
    }

    private fun CoroutineScope.setupServerNotificationsAsync() = launch {
        server.status.collect {
            when (it) {
                is ServerStatus.Started -> {
                    notifyInfo(R.strings.serverTitle, R.strings.serverRunningMsg)
                }
                is ServerStatus.PortTakenErr -> {

                    if (it.retryPort != null)
                        notifyWarn(
                            R.strings.serverTitle,
                            R.strings.portTakenMsg(it.failedPort) + " " + R.strings.portRetryMsg(it.retryPort)
                        )
                    else
                        notifyErr(
                            R.strings.serverTitle.failed(),
                            R.strings.portTakenMsg(it.failedPort) + " " + R.strings.allPortFailedMsg()
                        )
                }
                ServerStatus.Stopped -> notifyInfo(R.strings.serverTitle, R.strings.serverStoppedMsg)
                else -> {
                }
            }
        }
    }
}