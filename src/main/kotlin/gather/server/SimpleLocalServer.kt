package gather.server

import common.helpers.ioScope
import common.helpers.notifyErr
import common.helpers.notifyWarn
import common.res.R
import common.res.failed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException

abstract class SimpleLocalServer(private val ports: List<Int>, private val timeout: Int) {
    private var serverSocket: ServerSocket? = null
    private val scope = ioScope()

    fun startServer() {
        if (isActive()) return

        var portIndex = 0

        while (portIndex < ports.size) {

            if (portIndex != 0)
                notifyWarn(
                    R.strings.problemGatheringTitle.failed(),
                    R.strings.portTakenMsg(ports[portIndex - 1]) + " " + R.strings.portRetryMsg(ports[portIndex])
                )

            try {
                serverSocket = ServerSocket(ports[portIndex], 50, InetAddress.getByName("localhost"))
                onServerStart(serverSocket!!)
                // successfully started the server
                return
            } catch (e: SocketException) {
                // failed retrying with next port
                portIndex++
            }
        }

        if (portIndex != 0)
            notifyErr(
                R.strings.problemGatheringTitle.failed(),
                R.strings.portTakenMsg(ports[portIndex - 1]) + " " + R.strings.allPortFailedMsg()
            )
    }

    private fun onServerStart(serverSocket: ServerSocket) {
        serverSocket.soTimeout = timeout
        scope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    serverSocket.accept().use {
                        val inputStream = it.getInputStream()
                        val request = readFromStream(inputStream)
                        val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

                        if (strings.size > 1)
                            launch(Dispatchers.Default) { onMessage(strings[1]) }
                    }
                } catch (e: SocketTimeoutException) {
                    launch(Dispatchers.Default) { onTimeout() }
                } catch (e: SocketException) {
                    e.printStackTrace()
                    // socket maybe closed, catch and ignore it
                }
            }
        }
    }

    abstract suspend fun onMessage(message: String)

    abstract suspend fun onTimeout()


    private fun isActive(): Boolean {
        return serverSocket != null && !serverSocket!!.isClosed
    }

    private fun readFromStream(inputStream: InputStream): String {
        BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) builder.append(line).append('\n')
            return builder.toString()
        }
    }
}
