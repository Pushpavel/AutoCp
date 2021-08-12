package gather.models

import database.models.Problem

sealed interface GatheringResult {
    data class Gathered(val problems: List<Problem>, val batch: BatchJson) : GatheringResult
    data class Cancelled(val problems: List<Problem>, val batch: BatchJson) : GatheringResult

    object JsonErr : GatheringResult
    data class IncompleteErr(val problems: List<Problem>, val batch: BatchJson) : GatheringResult
}