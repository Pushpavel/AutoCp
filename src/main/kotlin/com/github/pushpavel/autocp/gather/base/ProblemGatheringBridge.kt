package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.models.ProblemJson
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

object ProblemGatheringBridge {
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
                    while (true) {
                        try {
                            val message = listenForMessage(
                                serverSocket, R.others.problemGatheringTimeoutMillis
                            ).await() ?: continue
                            val json = serializer.decodeFromString<ProblemJson>(message)
                            ProblemGatheringPipeline.onJsonReceived(json)
                        } catch (e: SocketTimeoutException) {
                            ProblemGatheringPipeline.onJsonTimeout()
                        } catch (e: SerializationException) {
                            ProblemGatheringPipeline.onJsonErr()
                        }
                    }
                }


            } catch (e: ProblemGatheringErr) {
                R.notify.problemGatheringErr(e)
            } catch (e: Exception) {
                R.notify.problemGatheringUncaught(e)
            }
        }
    }

}
