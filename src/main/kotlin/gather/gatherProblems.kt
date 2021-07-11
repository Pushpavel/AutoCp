package gather

import com.google.gson.Gson
import com.github.pushpavel.autocp.database.Problem
import gather.models.ProblemGatheredEvent
import gather.models.ProblemJson
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

/**
 * receives jsonStrings from @param responses and gathers
 * the [Problem] objects, while notifying updates through @param events channel
 */
suspend fun gatherProblems(
    responses: ReceiveChannel<String>,
    events: SendChannel<ProblemGatheredEvent>
): ArrayList<Problem>? {

    val gson = Gson()
    var parsingBatchId: String? = null
    val list = ArrayList<Problem>()

    for (response in responses) {
        val json = gson.fromJson(response, ProblemJson::class.java)

        if (parsingBatchId != null && json.batch.id != parsingBatchId)
            continue

        val problem = json.toProblem()

        list.add(problem)
        parsingBatchId = json.batch.id

        events.send(ProblemGatheredEvent(problem, list.size, json.batch.size))

        if (json.batch.size == list.size)
            return list
    }
    return null
}