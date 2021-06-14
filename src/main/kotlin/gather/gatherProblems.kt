package gather

import com.google.gson.Gson
import gather.models.GatheredProblem
import gather.models.ProblemGatheredEvent
import gather.models.ProblemJson
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

suspend fun gatherProblems(
    responses: ReceiveChannel<String>,
    events: SendChannel<ProblemGatheredEvent>
): ArrayList<GatheredProblem>? {

    val gson = Gson()
    var parsingBatchId: String? = null
    val list = ArrayList<GatheredProblem>()

    for (response in responses) {
        val json = gson.fromJson(response, ProblemJson::class.java)

        if (parsingBatchId != null && json.batch.id != parsingBatchId)
            continue

        val problem = json.toGatheredProblem()

        list.add(problem)
        parsingBatchId = json.batch.id

        events.send(ProblemGatheredEvent(problem, list.size, json.batch.size))

        if (json.batch.size == list.size)
            return list
    }

    return null
}


//suspend fun gatherProblems(project: Project, server: SimpleHttpPostHandler): ArrayList<GatheredProblem>? =
//    withContext(Dispatchers.Swing) {
//        val dialog = GatheringReporterDialog(project, server)
//        val gson = Gson()
//        var parsingBatchId: String? = null
//
//        val responses = server.getFlowStarter() ?: return@withContext null
//
//        responses.fold(ArrayList()) { list, jsonString ->
//
//            val json = gson.fromJson(jsonString, ProblemJson::class.java)
//
//            if (parsingBatchId != null && json.batch.id != parsingBatchId)
//                return@fold list
//
//            val problem = json.toGatheredProblem()
//
//            list.add(problem)
//            parsingBatchId = json.batch.id
//
//            withContext(Dispatchers.Swing) {
//                dialog.problemGathered(problem, list.size, json.batch.size)
//            }
//
//            // parsed all problems of the batch
//            if (json.batch.size == list.size)
//                server.stopServer()
//
//            list
//        }
//
//    }