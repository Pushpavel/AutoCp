package gather.server

import common.errors.NoReachErr
import common.helpers.catchAndLog
import database.models.Problem
import gather.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


/**
 * parses [messages] from server
 * also makes sure only one batch's problems are parsed at a time
 * and ignores possible incomplete batches beforehand
 */
class ProblemGathering(
    private val scope: CoroutineScope,
    private val messages: MutableSharedFlow<ServerMessage>,
) {
    val gathers = MutableSharedFlow<GatheringResult>()

    private val serializer = Json { ignoreUnknownKeys = true }
    private var currentBatch: BatchJson? = null
    private val ignoredBatches = mutableSetOf<BatchJson>()
    private val parsedProblems = mutableListOf<Problem>()

    init {
        scope.launch {
            messages.collect { catchAndLog { handleServerMessage(it) } }
        }
    }

    private suspend fun handleServerMessage(it: ServerMessage) {
        when (it) {
            is ServerMessage.Success -> {
                val result = deserializeMessage(it.message) ?: return
                when (result) {
                    is GatheringResult.Gathered -> {
                        gathers.emit(result)

                        if (result.isCompleted())
                            clearBatch()
                    }
                    is GatheringResult.JsonErr -> {
                        gathers.emit(result)
                        clearBatch()
                    }
                    else -> throw NoReachErr
                }
            }
            is ServerMessage.Err -> {
                gathers.emit(GatheringResult.ServerErr(it, parsedProblems.toList(), currentBatch))
                clearBatch()
            }
        }
    }


    private fun deserializeMessage(message: String): GatheringResult? {
        try {
            val json = serializer.decodeFromString<ProblemJson>(message)
            val problem = json.toProblem()
            val batch = json.batch

            // ignoring batches which is not currentBatch or already ignored
            if (ignoredBatches.contains(batch) || (currentBatch != null && currentBatch != batch)) {
                ignoredBatches.add(batch)
                return null
            }

            parsedProblems.add(problem)
            currentBatch = batch

            return GatheringResult.Gathered(parsedProblems.toList(), batch)
        } catch (e: SerializationException) {
            return GatheringResult.JsonErr(parsedProblems.toList(), currentBatch)
        }
    }

    fun cancelBatch() {
        currentBatch?.let {
            scope.launch {
                gathers.emit(GatheringResult.Cancelled(parsedProblems.toList(), it))
            }
        }
    }

    fun interruptBatch(err: Exception) {
        currentBatch?.let {
            scope.launch {
                gathers.emit(GatheringResult.Interrupted(err, parsedProblems.toList(), it))
            }
        }
    }

    private fun clearBatch() {
        currentBatch?.let { ignoredBatches.add(it) }
        parsedProblems.clear()
        currentBatch = null
    }
}
