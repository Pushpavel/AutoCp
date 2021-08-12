package gather.server

import gather.models.ServerMessage
import gather.models.ServerStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException


/**
 * Starts a simple server using [Dispatchers.IO]
 * also attempts to retry with different [ports]
 * status of the server are communicated through [status]
 */
fun CoroutineScope.startServerAsync(ports: List<Int>, status: MutableStateFlow<ServerStatus>) {
    if (status.value !is ServerStatus.Idle)
        return

    launch(Dispatchers.IO) {
        status.emit(ServerStatus.Starting)

        var portIndex = 0

        while (portIndex < ports.size) {

            if (portIndex != 0)
                status.emit(ServerStatus.PortTakenErr(ports[portIndex - 1], ports[portIndex]))

            try {
                val serverSocket = ServerSocket(ports[portIndex], 50, InetAddress.getByName("localhost"))
                // successfully started the server
                status.emit(ServerStatus.Started(serverSocket))
                return@launch
            } catch (e: SocketException) {
                // failed retrying with next port
                portIndex++
            }
        }

        if (portIndex != 0)
            status.emit(ServerStatus.PortTakenErr(ports[portIndex - 1], null))

        status.emit(ServerStatus.Idle)
    }
}

/**
 * listens to [status] and closes the serverSocket received in [ServerStatus.Started] event
 * during the [ServerStatus.Stopped] event and also resets back to [ServerStatus.Idle]
 */
suspend fun setupServerStopper(status: MutableSharedFlow<ServerStatus>) = coroutineScope {
    var serverSocket: ServerSocket? = null

    status.collect {

        if (it is ServerStatus.Started)
            serverSocket = it.serverSocket
        else if (it is ServerStatus.Stopped && serverSocket?.isClosed == false)
            launch(Dispatchers.IO) {
                serverSocket?.close()
                status.emit(ServerStatus.Idle)
            }
    }
}

/**
 * Accepts connection to server from [status] and
 * sends [messages] coming to the server
 */
fun CoroutineScope.getServerMessagesAsync(
    timeout: Int,
    status: MutableStateFlow<ServerStatus>,
    messages: MutableSharedFlow<ServerMessage>
) = launch(Dispatchers.IO) {

    var currentJob: Job? = null

    status.collect {
        when (it) {
            is ServerStatus.Started -> {
                currentJob = launch(Dispatchers.IO) {
                    while (true) {
                        val result = getMessageBlocking(it.serverSocket, timeout) ?: break
                        messages.emit(result)
                    }
                }
            }
            else -> currentJob?.cancel()
        }
    }
}

/**
 * Blocks till a connection is available and returns the message sent to the [serverSocket]
 */
@Suppress("BlockingMethodInNonBlockingContext")
private fun getMessageBlocking(serverSocket: ServerSocket, timeout: Int): ServerMessage? {
    try {
        serverSocket.accept().use {
            serverSocket.soTimeout = timeout
            val inputStream = it.getInputStream()
            val request = readFromStream(inputStream)
            val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

            if (strings.size > 1)
                return ServerMessage.Success(strings[1])
        }
    } catch (e: SocketTimeoutException) {
        serverSocket.soTimeout = 0
        return ServerMessage.TimeoutErr
    } catch (e: SocketException) {
        e.printStackTrace()
        // socket maybe closed, catch and ignore it
    }

    return null
}

/**
 * Converts inputStream to String
 */
private fun readFromStream(inputStream: InputStream): String {
    BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
        val builder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) builder.append(line).append('\n')
        return builder.toString()
    }
}