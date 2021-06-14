package gather.result

import gather.models.GatheredProblem

interface ProblemGatherListener {
    fun problemGathered(problem: GatheredProblem, problemsGathered: Int, totalProblems: Int)
}