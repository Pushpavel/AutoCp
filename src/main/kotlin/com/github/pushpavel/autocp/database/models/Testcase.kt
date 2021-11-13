package com.github.pushpavel.autocp.database.models

import kotlinx.serialization.Serializable

/**
 * Model class representing a single Testcase of a [Problem][com.github.pushpavel.autocp.database.models.Problem]
 */
@Serializable
data class Testcase(
    val name: String,
    val input: String,
    val output: String,
)