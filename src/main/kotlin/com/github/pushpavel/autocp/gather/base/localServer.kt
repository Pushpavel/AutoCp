package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.BufferedReader
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException


/**
 * Try to bind server sockets on loopback addresses for a port from [ports].
 *
 * Competitive Companion always sends to `http://localhost:<port>/`.
 * On some systems `localhost` can resolve to IPv6 first (`::1`), while AutoCp may only be bound to IPv4 (`127.0.0.1`)
 * (or vice versa). To make this robust, we try to bind both loopback addresses on the same port (if the OS allows it).
 */
fun CoroutineScope.openServerSocketsAsync(ports: List<Int>) = async(Dispatchers.IO) {
    val log = Logger.getInstance("${R.keys.pluginId} openServerSocketAsync")
    var portIndex = 0

    while (portIndex < ports.size) {

        if (portIndex != 0)
            log.info("Port ${ports[portIndex - 1]} taken. retrying with Port ${ports[portIndex]}")

        val port = ports[portIndex]
        val sockets = mutableListOf<ServerSocket>()
        try {
            // Prefer IPv4 loopback to keep current behavior, then also try IPv6 loopback.
            runCatching { sockets.add(ServerSocket(port, 50, InetAddress.getByName("127.0.0.1"))) }
            runCatching { sockets.add(ServerSocket(port, 50, InetAddress.getByName("::1"))) }

            if (sockets.isNotEmpty()) {
                runCatching {
                    val bound = sockets.joinToString(", ") { s -> s.inetAddress.hostAddress + ":" + s.localPort }
                    log.info("Listening for Competitive Companion on $bound")
                }
                return@async sockets
            }
        } catch (_: SocketException) {
            // ignore and retry next port
        } catch (_: Exception) {
            // ignore and retry next port
        } finally {
            if (sockets.isEmpty()) {
                // Ensure we don't leak partially opened sockets when retrying.
                sockets.forEach { runCatching { it.close() } }
            }
        }

        portIndex++
    }

    if (portIndex != 0)
        log.info("Port ${ports[portIndex - 1]} taken. no more ports to retry :(")

    return@async null
}

/**
 * Blocks till a connection is available and returns the message sent to the [serverSocket]
 */
fun CoroutineScope.listenForMessageAsync(serverSocket: ServerSocket, timeout: Int) = async(Dispatchers.IO) {
    serverSocket.soTimeout = timeout
    try {
        serverSocket.accept().use {
            // ServerSocket.soTimeout only affects accept(). Ensure reads don't block forever.
            it.soTimeout = timeout

            val inputStream = BufferedInputStream(it.getInputStream())
            val body = readHttpBody(inputStream)

            if (!body.isNullOrEmpty()) return@async body
        }
        return@async null
    } catch (_: SocketTimeoutException) {
        // Normal: no incoming connection within timeout.
        return@async null
    } catch (_: SocketException) {
        // Can happen on shutdown / dynamic plugin unload.
        return@async null
    }
}

/**
 * Reads an HTTP request body without waiting for EOF.
 * Supports both CRLF and LF separators, and uses Content-Length when available.
 */
private fun readHttpBody(inputStream: BufferedInputStream): String? {
    val headerBytes = ByteArrayOutputStream()
    var prev = -1
    var curr: Int
    var seenLfLf = false
    var seenCrlfCrlf = false

    // Read headers up to a sane limit
    val maxHeaderBytes = 64 * 1024
    while (headerBytes.size() < maxHeaderBytes) {
        curr = inputStream.read()
        if (curr == -1) break
        headerBytes.write(curr)

        // detect \n\n
        if (prev == '\n'.code && curr == '\n'.code) {
            seenLfLf = true
            break
        }
        // detect \r\n\r\n
        val hb = headerBytes.toByteArray()
        val n = hb.size
        if (n >= 4 &&
            hb[n - 4] == '\r'.code.toByte() &&
            hb[n - 3] == '\n'.code.toByte() &&
            hb[n - 2] == '\r'.code.toByte() &&
            hb[n - 1] == '\n'.code.toByte()
        ) {
            seenCrlfCrlf = true
            break
        }

        prev = curr
    }

    if (!seenLfLf && !seenCrlfCrlf) {
        // couldn't find header terminator; fall back to old behavior
        return null
    }

    val headers = headerBytes.toString(Charsets.ISO_8859_1)
    val contentLength = Regex("(?im)^Content-Length:\\s*(\\d+)\\s*$")
        .find(headers)
        ?.groupValues
        ?.getOrNull(1)
        ?.toIntOrNull()
        ?: return null

    if (contentLength <= 0) return null

    val bodyBytes = ByteArray(contentLength)
    var off = 0
    while (off < contentLength) {
        val read = inputStream.read(bodyBytes, off, contentLength - off)
        if (read <= 0) break
        off += read
    }
    if (off <= 0) return null
    return bodyBytes.copyOf(off).toString(Charsets.UTF_8)
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