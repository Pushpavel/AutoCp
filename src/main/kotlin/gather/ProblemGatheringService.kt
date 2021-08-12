package gather

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import common.helpers.ioScope
import common.helpers.notifyErr
import common.helpers.notifyInfo
import common.helpers.notifyWarn
import common.res.R
import common.res.failed
import common.res.success
import gather.models.GatheringResult
import gather.models.ServerMessage
import gather.models.ServerStatus
import gather.server.ProblemGathering
import gather.server.getServerMessagesAsync
import gather.server.setupServerStopper
import gather.server.startServerAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Service
class ProblemGatheringService(val project: Project) {
    private val scope = ioScope()

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
                    notifyInfo(R.strings.serverTitle, R.strings.serverRunningMsg(it.serverSocket.localPort))
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
                        R.strings.problemGatheringTitle.failed(),
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
}