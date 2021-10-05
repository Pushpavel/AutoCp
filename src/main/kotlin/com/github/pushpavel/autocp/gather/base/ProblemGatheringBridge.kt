package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.intellij.openapi.Disposable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

/**
 * Bridge between Competitive Companion extension and [BatchProcessor]
 */
object ProblemGatheringBridge : Disposable {
    private val scope = ioScope()
    private val serializer = Json { ignoreUnknownKeys = true }

    init {
        // initialize server
        scope.launch {
            try {
                val serverSocket = openServerSocketAsync(
                    R.others.competitiveCompanionPorts
                ).await() ?: throw ProblemGatheringErr.AllPortsTakenErr(R.others.competitiveCompanionPorts)

                serverSocket.use {
                    try {
                        while (true) {
                            try {
                                val message = listenForMessage(
                                    serverSocket, R.others.problemGatheringTimeoutMillis
                                ).await() ?: continue
                                val json = serializer.decodeFromString<ProblemJson>(message)
                                BatchProcessor.onJsonReceived(json)
                            } catch (e: SocketTimeoutException) {
                                BatchProcessor.interruptBatch(ProblemGatheringErr.TimeoutErr)
                            } catch (e: SerializationException) {
                                BatchProcessor.interruptBatch(ProblemGatheringErr.JsonErr)
                            }
                            while (BatchProcessor.isCurrentBatchBlocking()) delay(100)
                        }
                    } finally {
                        BatchProcessor.interruptBatch()
                    }
                }


            } catch (e: ProblemGatheringErr) {
                R.notify.problemGatheringErr(e)
            } catch (e: Exception) {
                R.notify.problemGatheringUncaught(e)
            }
        }
    }

    override fun dispose() = scope.cancel()

}
