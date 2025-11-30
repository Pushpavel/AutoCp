package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.models.ProblemJson
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

/**
 * Bridge between Competitive Companion extension and [BatchProcessor]
 * Application-level service to ensure only one instance is running
 */
@Service(Service.Level.APP)
class ProblemGatheringBridge : Disposable {
    private val scope = ioScope()
    private val serializer = Json { ignoreUnknownKeys = true }
    
    @Volatile
    private var isRunning = false
    
    @Volatile
    private var serverJob: Job? = null

    companion object {
        fun getInstance(): ProblemGatheringBridge {
            return ApplicationManager.getApplication().getService(ProblemGatheringBridge::class.java)
        }
    }

    fun start() {
        if (isRunning) {
            return
        }
        
        isRunning = true
        
        // initialize server
        serverJob = scope.launch {
            try {
                val serverSocket = openServerSocketAsync(
                    R.others.competitiveCompanionPorts
                ).await() ?: throw ProblemGatheringErr.AllPortsTakenErr(R.others.competitiveCompanionPorts)

                serverSocket.use {
                    while (isActive) {
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
                if (e !is CancellationException) {
                    R.notify.problemGatheringUncaught(e)
                }
            } finally {
                isRunning = false
                BatchProcessor.interruptBatch()
            }
        }
    }

    fun stop() {
        serverJob?.cancel()
        isRunning = false
    }
    
    override fun dispose() {
        stop()
    }
}

/**
 * Starts the ProblemGatheringBridge service when the first project opens
 */
class ProblemGatheringBridgeStarter : ProjectActivity, DumbAware {
    override suspend fun execute(project: Project) {
        ProblemGatheringBridge.getInstance().start()
    }
}
