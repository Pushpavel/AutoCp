package plugin.services

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import common.AutoCpProblem
import common.ProblemJson

@Service
class GatherProblemsService {
    private val gson: Gson by lazy { Gson() }

    companion object {
        private const val CPH_PORT = 27121
    }

    private var server: SimpleHttpPostServer? = null

    fun gatherProblems(): List<AutoCpProblem>? {
        if (server != null)
            return null
        val problems = ArrayList<AutoCpProblem>()
        var parsingBatchId: String? = null

        server = SimpleHttpPostServer(CPH_PORT)

        server?.let {
            for (res in it.responses) {
                print("Got Response !\n")
                val json = gson.fromJson(res, ProblemJson::class.java)

                if (parsingBatchId != null && json.batch.id != parsingBatchId)
                    continue

                val problem = AutoCpProblem(json)

                problems.add(problem)

                parsingBatchId = problem.batchId

                // parsed all problems of the batch
                if (problem.batchLength == problems.size) {
                    stopGathering()
                    return problems
                }
            }
        }
        stopGathering()
        return null
    }

    fun stopGathering() {
        server?.dispose()
        server = null
    }

    fun isGathering() {
        return isGathering()
    }
}