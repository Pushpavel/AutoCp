package services

import com.intellij.openapi.Disposable
import kotlinx.coroutines.yield
import org.jetbrains.concurrency.runAsync
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket


class SimpleHttpPostServer(port: Int) : Disposable {
    private val serverSocket = ServerSocket(port, 50, InetAddress.getByName("localhost"))

    val responses = sequence {
        while (true) {

            if (serverSocket.isClosed) return@sequence

            try {

                serverSocket.accept().use {
                    val inputStream = it.getInputStream()
                    val request = readFromStream(inputStream)
                    val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

                    if (strings.size > 1)
                        yield(strings[1])
                }

            } catch (_: IOException) {
                return@sequence
            }
        }
    }


    override fun dispose() = serverSocket.close()
}

fun listenLocalHttpPost(port: Int, handler: (String?, () -> Unit) -> Unit): () -> Unit {
    val serverSocket = ServerSocket(port, 50, InetAddress.getByName("localhost"))

    val stopServer = fun() { runCatching { serverSocket.close() } }
    runAsync {
        while (true) {
            runCatching {

                serverSocket.accept().use {

                    val inputStream = it.getInputStream()
                    val request = readFromStream(inputStream)
                    val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

                    if (strings.size < 2)
                        return@runCatching

                    handler(strings[1], stopServer)
                }

            }.onFailure {
                return@runAsync
            }
        }
    }

    return stopServer
}

fun readFromStream(inputStream: InputStream): String {
    BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
        val builder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) builder.append(line).append('\n')
        return builder.toString()
    }
}