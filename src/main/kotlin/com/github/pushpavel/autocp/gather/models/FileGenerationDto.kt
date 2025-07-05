package com.github.pushpavel.autocp.gather.models

import kotlinx.serialization.Serializable

@Serializable
data class FileGenerationDto(
    val fileName: String,
    val rootDir: String,
    val template: String?
)
