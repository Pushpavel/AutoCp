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
import common.res.cancelled
import common.res.failed
import common.res.success
import database.autoCp
import database.models.Problem
import gather.models.GatheringResult
import gather.models.GenerateFileErr
import gather.models.ServerMessage
import gather.models.ServerStatus
import gather.server.ProblemGathering
import gather.server.getServerMessagesAsync
import gather.server.setupServerStopper
import gather.server.startServerAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import settings.generalSettings.AutoCpGeneralSettings
import settings.generalSettings.OpenFileOnGather
import settings.projectSettings.autoCpProject
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

@Service
class ProblemGatheringService(val project: Project) {
    private val scope = ioScope()
    private val db = project.autoCp()

    private val server = MutableStateFlow<ServerStatus>(ServerStatus.Idle)
    private val messages = MutableSharedFlow<ServerMessage>()
    private val gathers = MutableSharedFlow<GatheringResult>()
    private val gathering = ProblemGathering(scope, messages, gathers)

    init {
        // setup the pipeline
        scope.launch { setupServerStopper(server) }
        ProgressReporter(scope, project, gathers, gathering::cancelBatch)
        scope.getServerMessagesAsync(R.others.problemGatheringTimeoutMillis, server, messages)


    }

    fun startServiceAsync() = scope.startServerAsync(R.others.competitiveCompanionPorts, server)

    fun isRunning() = server.value !is ServerStatus.Idle

    fun stopService() {
        gathering.cancelBatch()
        server.value = ServerStatus.Stopped
    }

    // Processing the Problems

    init {
        scope.addGathersToDBAsync()
    }

    private fun CoroutineScope.addGathersToDBAsync() = launch {
        gathers.collect {
            if (it is GatheringResult.Gathered) {
                db.updateProblem(it.problems.last())
                try {

                    val openFile = when (AutoCpGeneralSettings.instance.openFilesOnGather) {
                        OpenFileOnGather.NONE -> false
                        OpenFileOnGather.ONLY_FIRST -> it.problems.size == 1
                        OpenFileOnGather.ALL -> true
                    }

                    generateFileAsync(it.problems.last(), openFile)

                } catch (err: Exception) {
                    notifyGenerateFileErr(err, it.problems.last())
                }
            }
        }
    }

    private fun CoroutineScope.generateFileAsync(problem: Problem, open: Boolean) = launch(Dispatchers.IO) {
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
            setupGatheringNotificationsAsync()
        }
    }

    private fun CoroutineScope.setupServerNotificationsAsync() = launch {
        server.collect {
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

    private fun CoroutineScope.setupGatheringNotificationsAsync() = launch {
        gathers.collect {
            when (it) {
                is GatheringResult.Gathered -> {
                    if (it.problems.size == it.batch.size) {
                        notifyInfo(
                            R.strings.problemGatheringTitle.success(),
                            R.strings.gatheredAllProblems(
                                it.problems.first().groupName,
                                it.problems
                            )
                        )
                    }
                }
                is GatheringResult.Cancelled -> {
                    notifyWarn(
                        R.strings.problemGatheringTitle.cancelled(),
                        R.strings.gatheringProblemsCancelled(
                            it.problems.first().groupName,
                            it.problems,
                            it.batch.size
                        )
                    )
                }
                is GatheringResult.IncompleteErr -> {
                    notifyErr(
                        R.strings.problemGatheringTitle.failed(),
                        R.strings.incompleteProblemsGathering(
                            it.problems.first().groupName,
                            it.problems,
                            it.batch.size
                        )
                    )
                }
                GatheringResult.JsonErr -> {
                    notifyErr(
                        R.strings.problemGatheringTitle.failed(),
                        R.strings.competitiveCompanionJsonFormatErrMsg
                    )
                }
            }
        }
    }


    private fun notifyGenerateFileErr(err: Exception, problem: Problem) {
        when (err) {
            is GenerateFileErr.FileAlreadyExistsErr -> notifyWarn(
                R.strings.fileGenFailedTitle(Path(err.filePath).name),
                R.strings.fileAlreadyExistsMsg(err)
            )
            is GenerateFileErr.FileTemplateMissingErr -> notifyErr(
                R.strings.fileGenFailedTitle(problem.name),
                R.strings.fileTemplateMissingMsg(err)
            )
            is GenerateFileErr.LangNotConfiguredErr -> notifyErr(
                R.strings.fileGenFailedTitle(problem.name),
                R.strings.langNotConfiguredMsg
            )
            else -> notifyErr(
                R.strings.fileGenFailedTitle(problem.name),
                R.strings.defaultFileIssue(err)
            )
        }
    }
}