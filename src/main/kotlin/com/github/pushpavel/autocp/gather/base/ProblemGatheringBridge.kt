package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

/**
 * Bridge between Competitive Companion extension and [BatchProcessor]
 */
class ProblemGatheringBridge : ProjectActivity, DumbAware {
    private val scope = ioScope()
    private val serializer = Json { ignoreUnknownKeys = true }

    fun runActivity(project: Project) {
        // initialize server
        scope.launch {
            try {
                val serverSocket = openServerSocketAsync(
                    R.others.competitiveCompanionPorts
                ).await() ?: throw ProblemGatheringErr.AllPortsTakenErr(R.others.competitiveCompanionPorts)

                serverSocket.use {
                    while (true) {
                        try {
                            coroutineScope {
                                val message = listenForMessageAsync(
                                    serverSocket, R.others.problemGatheringTimeoutMillis
                                ).await() ?: return@coroutineScope

                                val json = serializer.decodeFromString<ProblemJson>(message)
                                BatchProcessor.onJsonReceived(json)
                            }
                        } catch (e: SocketTimeoutException) {
                            BatchProcessor.interruptBatch(ProblemGatheringErr.TimeoutErr)
                        } catch (e: SerializationException) {
                            BatchProcessor.interruptBatch(ProblemGatheringErr.JsonErr)
                        }

                        while (BatchProcessor.isCurrentBatchBlocking()) delay(100)
                    }
                }
            } catch (e: ProblemGatheringErr) {
                R.notify.problemGatheringErr(e)
            } catch (e: Exception) {
                R.notify.problemGatheringUncaught(e)
            } finally {
                BatchProcessor.interruptBatch()
            }
        }
    }

    override suspend fun execute(project: Project) = runActivity(project)
}
