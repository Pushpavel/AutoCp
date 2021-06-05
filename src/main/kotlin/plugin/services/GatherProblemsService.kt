package plugin.services

import com.google.gson.Gson
import com.intellij.openapi.components.Service
import files.ProblemSpec

@Service
class GatherProblemsService {
    private val gson: Gson by lazy { Gson() }

    companion object {
        private const val CPH_PORT = 27121
    }

    private var server: SimpleHttpPostServer? = null

    fun gatherProblems(): List<ProblemSpec>? {
        if (server != null)
            return null
        val problems = ArrayList<ProblemSpec>()
        var parsingBatchId: String? = null

        server = SimpleHttpPostServer(CPH_PORT)

        server?.let {
            for (res in it.responses) {
                val json = gson.fromJson(res, ProblemJson::class.java)

                if (parsingBatchId != null && json.batch.id != parsingBatchId)
                    continue

                val problem = ProblemSpec(json)

                problems.add(problem)

                parsingBatchId = json.batch.id

                // parsed all problems of the batch
                if (json.batch.size == problems.size) {
                    stopGathering()
                    return problems
                }
            }
        }
        stopGathering()
        return null
    }

    private fun stopGathering() {
        server?.dispose()
        server = null
    }

    fun isGathering(): Boolean {
        return server != null
    }
}