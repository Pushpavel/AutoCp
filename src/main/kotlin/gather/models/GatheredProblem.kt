package gather.models

import database.models.ProblemInfo
import database.models.TestcaseSpec

data class GatheredProblem(
    val info: ProblemInfo,
    val testcases: List<TestcaseSpec>
)