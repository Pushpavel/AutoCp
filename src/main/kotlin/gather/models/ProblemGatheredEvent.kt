package gather.models

data class ProblemGatheredEvent(
    val gatheredProblem: GatheredProblem,
    val gathered: Int,
    val total: Int,
)