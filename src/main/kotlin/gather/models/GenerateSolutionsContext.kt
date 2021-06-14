package gather.models

import database.models.ProblemInfo
import plugin.settings.SolutionLanguage

data class GenerateSolutionsContext(
    val problems: List<ProblemInfo>,
    val lang: SolutionLanguage,

    )
