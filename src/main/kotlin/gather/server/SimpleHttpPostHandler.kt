package gather.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException

fun createServer(port: Int) = ServerSocket(port, 50, InetAddress.getByName("localhost"))

@ExperimentalCoroutinesApi
@Suppress("BlockingMethodInNonBlockingContext")
fun CoroutineScope.getResponsesAsync(server: ServerSocket, timeout: Long = Long.MAX_VALUE) = produce {
    try {
        server.soTimeout = timeout.toInt()
        while (true) {
            withContext(Dispatchers.IO) {
                server.accept().use {
                    val inputStream = it.getInputStream()
                    val request = readFromStream(inputStream)
                    val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

                    if (strings.size > 1)
                        send(strings[1])

                }
            }
        }
    } catch (e: SocketException) {
//        socket is closed
    }
}

private fun readFromStream(inputStream: InputStream): String {
    BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
        val builder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) builder.append(line).append('\n')
        return builder.toString()
    }
}