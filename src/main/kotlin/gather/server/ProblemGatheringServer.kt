package gather.server

import common.helpers.notifyErr
import common.helpers.notifyInfo
import common.helpers.notifyWarn
import common.res.R
import common.res.cancelled
import common.res.failed
import common.res.success
import database.models.Problem
import gather.models.BatchJson
import gather.models.ProblemJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class ProblemGatheringServer(
    private val listener: ProblemGatheringServerListener
) : SimpleLocalServer(R.others.competitiveCompanionPorts, R.others.problemGatheringTimeoutMillis) {

    private val serializer = Json { ignoreUnknownKeys = true }

    private val ignoredBatches = mutableSetOf<BatchJson>()

    private var currentBatch: BatchJson? = null
    private var parsedProblems = mutableListOf<Problem>()

    override suspend fun onMessage(message: String) {
        val (problem, batch) = deserializeJson(message) ?: return

        if (currentBatch != null && currentBatch != batch) {
            ignoredBatches.add(batch)
            return // ignoring different batches
        }

        if (ignoredBatches.contains(batch))
            return

        if (currentBatch == null)
            listener.onBatchStart()

        parsedProblems.add(problem)
        currentBatch = batch

        listener.onProblem(parsedProblems, batch)

        if (batch.size == parsedProblems.size) {
            // all problems gathered
            notifyInfo(
                R.strings.problemGatheringTitle.success(),
                R.strings.gatheredAllProblems(parsedProblems.first().groupName, parsedProblems)
            )

            clearBatch()
        }
    }

    override suspend fun onTimeout() {
        if (currentBatch == null)
            return

        notifyErr(
            R.strings.problemGatheringTitle.failed(),
            R.strings.incompleteProblemsGathering(
                parsedProblems.first().groupName,
                parsedProblems,
                currentBatch!!.size
            )
        )

        clearBatch()
    }

    fun cancelCurrentBatch() {
        currentBatch?.let {
            ignoredBatches.add(it)
            notifyWarn(
                R.strings.problemGatheringTitle.cancelled(),
                R.strings.gatheringProblemsCancelled(parsedProblems.first().groupName, parsedProblems, it.size)
            )
        }

        clearBatch()
    }

    private fun clearBatch() {
        listener.onBatchEnd()
        parsedProblems.clear()
        currentBatch = null
    }

    private fun deserializeJson(message: String): Pair<Problem, BatchJson>? = try {

        val json = serializer.decodeFromString<ProblemJson>(message)
        Pair(json.toProblem(), json.batch)

    } catch (e: Exception) {

        notifyErr(R.strings.problemGatheringTitle.failed(), R.strings.competitiveCompanionJsonFormatErrMsg)
        null
    }
}