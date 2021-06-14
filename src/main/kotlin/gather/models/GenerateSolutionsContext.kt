package gather.models

import database.models.ProblemSpec
import plugin.settings.SolutionLanguage

data class GenerateSolutionsContext(
    val problems: List<ProblemSpec>,
    val lang: SolutionLanguage,

    )
