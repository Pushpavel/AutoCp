package database

import database.models.Problem
import database.models.SolutionFile
import kotlinx.serialization.Serializable

@Serializable
data class AutoCpDB(
    val problems: Map<String, Map<String, Problem>> = mapOf(),
    val solutionFiles: Map<String, SolutionFile> = mapOf(),
)
