package gather.models

sealed interface ServerStatus {
    object Started : ServerStatus
    data class PortTakenErr(val failedPort: Int, val retryPort: Int?) : Exception(), ServerStatus
}

sealed interface ServerMessage {
    data class Success(val message: String) : ServerMessage
    object TimeoutErr : Exception(), ServerMessage
}