package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException


fun CoroutineScope.openServerSocketAsync(ports: List<Int>) = async(Dispatchers.IO) {
    val log = Logger.getInstance("${R.keys.pluginId} openServerSocketAsync")
    var portIndex = 0

    while (portIndex < ports.size) {

        if (portIndex != 0)
            log.info("Port ${ports[portIndex - 1]} taken. retrying with Port ${ports[portIndex]}")

        try {
            // successfully starting the server
            return@async ServerSocket(ports[portIndex], 50, InetAddress.getByName("localhost"))
        } catch (e: SocketException) {
            // failed retrying with next port
            portIndex++
        }
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
    serverSocket.accept().use {
        val inputStream = it.getInputStream()
        val request = readFromStream(inputStream)
        val strings = request.split("\n\n".toPattern(), 2).toTypedArray()

        if (strings.size > 1)
            return@async strings[1]
    }
    return@async null
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