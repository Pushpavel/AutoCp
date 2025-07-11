package com.github.pushpavel.autocp.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Problem(
    val name: String,
    val groupName: String,
    val url: String,
    val sampleTestcases: List<Testcase>,
    val memoryLimit: Long,
    val timeLimit: Long,
    val inputFile: String?,
    val outputFile: String?,
)
