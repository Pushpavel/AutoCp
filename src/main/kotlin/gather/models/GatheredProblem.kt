package gather.models

import database.models.ProblemSpec
import database.models.TestcaseSpec

data class GatheredProblem(
    val spec: ProblemSpec,
    val testcases: List<TestcaseSpec>
)