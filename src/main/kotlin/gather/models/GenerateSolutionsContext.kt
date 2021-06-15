package gather.models

import dev.pushpavel.autocp.database.Problem
import plugin.settings.SolutionLanguage

data class GenerateSolutionsContext(
    val problems: List<Problem>,
    val lang: SolutionLanguage,
)
