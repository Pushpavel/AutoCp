package gather

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import common.helpers.ioScope
import common.res.R
import gather.models.GatheringResult
import gather.models.ServerMessage
import gather.models.ServerStatus
import gather.server.ProblemGathering
import gather.server.getServerMessagesAsync
import gather.server.setupServerStopper
import gather.server.startServerAsync
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Service
class ProblemGatheringService(val project: Project) {
    private val scope = ioScope()

    // TODO: setup notifications from these flows
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

    fun stopService() {
        gathering.cancelBatch()
        server.value = ServerStatus.Stopped
    }

}