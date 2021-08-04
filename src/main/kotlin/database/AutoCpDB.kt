package database

import database.models.Problem
import database.models.SolutionFile

data class AutoCpDB(
    val problems: Map<String, Map<String, Problem>>,
    val solutionFiles: Map<String, SolutionFile>
)