package com.github.pushpavel.autocp.gather.models

import java.net.ServerSocket

sealed interface ServerStatus {
    object Idle : ServerStatus
    object Starting : ServerStatus
    object Stopped : ServerStatus
    data class Started(val serverSocket: ServerSocket) : ServerStatus
    data class PortTakenErr(val failedPort: Int, val retryPort: Int?) : Exception(), ServerStatus
}

sealed interface ServerMessage {
    data class Success(val message: String) : ServerMessage
    sealed interface Err : ServerMessage {
        object TimeoutErr : Exception(), Err
        object ServerStopped : Exception(), Err
    }
}
