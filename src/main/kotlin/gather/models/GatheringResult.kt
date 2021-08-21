package gather.models

import database.models.Problem

sealed interface GatheringResult {
    data class Gathered(val problems: List<Problem>, val batch: BatchJson) : GatheringResult
    data class Cancelled(val problems: List<Problem>, val batch: BatchJson) : GatheringResult

    data class Interrupted(
        val err: Exception,
        val problems: List<Problem>,
        val batch: BatchJson
    ) : GatheringResult

    data class ServerErr(
        val err: ServerMessage.Err,
        val problems: List<Problem>,
        val batch: BatchJson?
    ) : GatheringResult

    data class JsonErr(val problems: List<Problem>, val batch: BatchJson?) : GatheringResult
}

fun GatheringResult.Gathered.isCompleted() = problems.size == batch.size