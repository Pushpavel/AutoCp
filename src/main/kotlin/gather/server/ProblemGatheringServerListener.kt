package gather.server

import database.models.Problem
import gather.models.BatchJson

interface ProblemGatheringServerListener {
    fun onBatchStart()

    suspend fun onProblem(problems: List<Problem>, batch: BatchJson)

    fun onBatchEnd()
}