package gather.models

import java.net.ServerSocket

sealed interface ServerStatus {
    object Idle : ServerStatus
    object Starting : ServerStatus
    data class Started(val serverSocket: ServerSocket) : ServerStatus
    data class PortTakenErr(val failedPort: Int, val retryPort: Int?) : Exception(), ServerStatus
}

sealed interface ServerMessage {
    data class Success(val message: String) : ServerMessage
    object TimeoutErr : Exception(), ServerMessage
}