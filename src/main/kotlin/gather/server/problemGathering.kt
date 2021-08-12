package gather.server

import database.models.Problem
import gather.models.BatchJson
import gather.models.GatheringResult
import gather.models.ProblemJson
import gather.models.ServerMessage
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
    val scope: CoroutineScope,
    val messages: MutableSharedFlow<ServerMessage>,
    val gathers: MutableSharedFlow<GatheringResult>
) {

    private val serializer = Json { ignoreUnknownKeys = true }
    private var currentBatch: BatchJson? = null
    private val ignoredBatches = mutableSetOf<BatchJson>()
    private val parsedProblems = mutableListOf<Problem>()

    init {
        scope.launch {
            messages.collect {
                when (it) {
                    is ServerMessage.Success -> onMessage(it.message)
                    else -> {
                        currentBatch?.let { batch ->
                            gathers.emit(GatheringResult.IncompleteErr(parsedProblems.toList(), batch))
                        }
                        clearBatch()
                    }
                }
            }
        }
    }

    private suspend fun onMessage(message: String) {
        try {
            val json = serializer.decodeFromString<ProblemJson>(message)
            val problem = json.toProblem()
            val batch = json.batch

            // ignoring batches which is not currentBatch
            if (ignoredBatches.contains(batch) || (currentBatch != null && currentBatch != batch)) {
                ignoredBatches.add(batch)
                return
            }

            parsedProblems.add(problem)
            currentBatch = batch

            gathers.emit(GatheringResult.Gathered(parsedProblems.toList(), batch))

            if (batch.size == parsedProblems.size)
                clearBatch()

        } catch (e: SerializationException) {
            gathers.emit(GatheringResult.JsonErr)
            clearBatch()
        }
    }

    fun cancelBatch() {
        currentBatch?.let {
            scope.launch { gathers.emit(GatheringResult.Cancelled(parsedProblems.toList(), it)) }
            ignoredBatches.add(it)
            clearBatch()
        }
    }

    private fun clearBatch() {
        parsedProblems.clear()
        currentBatch = null
    }
}
