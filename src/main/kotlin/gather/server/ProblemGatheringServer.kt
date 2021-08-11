package gather.server

import common.helpers.notifyErr
import common.res.R
import common.res.failed
import database.models.Problem
import gather.models.BatchJson
import gather.models.ProblemJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class ProblemGatheringServer : SimpleLocalServer(R.others.competitiveCompanionPorts) {

    private val serializer = Json { ignoreUnknownKeys = true }
    private var currentBatch: BatchJson? = null
    private var parsedProblems = mutableListOf<Problem>()

    override suspend fun onMessage(message: String) {
        val (problem, batch) = deserializeJson(message) ?: return

        if (currentBatch != null && currentBatch != batch)
            return // ignoring different batches

        parsedProblems.add(problem)

        // TODO: utilize problem
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