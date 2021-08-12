package gather

import com.intellij.openapi.components.Service
import common.helpers.ioScope
import common.res.R
import gather.models.GatheringResult
import gather.models.ServerMessage
import gather.models.ServerStatus
import gather.server.ProblemGathering
import gather.server.getServerMessagesAsync
import gather.server.startServerAsync
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Service
class ProblemGatheringService {
    val scope = ioScope()
    val server = MutableStateFlow<ServerStatus>(ServerStatus.Idle)
    val messages = MutableSharedFlow<ServerMessage>()
    val gathers = MutableSharedFlow<GatheringResult>()
    val gathering = ProblemGathering(scope, messages, gathers)

    fun startServiceAsync() {
        if (server.value !is ServerStatus.Idle) return

        scope.launch {
            getServerMessagesAsync(R.others.problemGatheringTimeoutMillis, server, messages)

            startServerAsync(R.others.competitiveCompanionPorts, server)
        }
    }
}