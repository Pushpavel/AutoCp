package com.github.pushpavel.autocp.database

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.database.models.SolutionFile
import kotlinx.serialization.Serializable

@Serializable
data class AutoCpDB(
    val version: Int,
    val problems: Map<String, Map<String, Problem>> = mapOf(),
    val solutionFiles: Map<String, SolutionFile> = mapOf(),
)
