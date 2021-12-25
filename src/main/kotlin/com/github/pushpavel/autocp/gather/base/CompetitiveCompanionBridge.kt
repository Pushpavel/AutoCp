package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

/**
 * Bridge between Competitive Companion extension and [ProblemBatchProcessor]
 * Runs a local server on IDE start listening for problems parsed from competitive companion
 */
class CompetitiveCompanionBridge : StartupActivity, DumbAware {
    private val scope = ioScope()
    private val serializer = Json { ignoreUnknownKeys = true }

    override fun runActivity(project: Project) {
        // initialize server
        scope.launch {
            try {
                // start server
                val serverSocket = openServerSocketAsync(
                    R.others.competitiveCompanionPorts
                ).await() ?: throw ProblemGatheringErr.AllPortsTakenErr(R.others.competitiveCompanionPorts)

                // listen for messages and notify BatchProcessor
                serverSocket.use {
                    while (true) {
                        try {
                            coroutineScope {
                                val message = listenForMessageAsync(
                                    serverSocket, R.others.problemGatheringTimeoutMillis
                                ).await() ?: return@coroutineScope

                                val json = serializer.decodeFromString<ProblemJson>(message)
                                ProblemBatchProcessor.onJsonReceived(json)
                            }
                        } catch (e: SocketTimeoutException) {
                            ProblemBatchProcessor.interruptBatch(ProblemGatheringErr.TimeoutErr)
                        } catch (e: SerializationException) {
                            ProblemBatchProcessor.interruptBatch(ProblemGatheringErr.JsonErr)
                        }

                        while (ProblemBatchProcessor.isCurrentBatchBlocking()) delay(100)
                    }
                }
            } catch (e: ProblemGatheringErr) {
                R.notify.problemGatheringErr(e)
            } catch (e: Exception) {
                R.notify.problemGatheringUncaught(e)
            } finally {
                ProblemBatchProcessor.interruptBatch()
            }
        }
    }
}
