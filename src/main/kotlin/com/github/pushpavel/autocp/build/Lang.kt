package com.github.pushpavel.autocp.build

data class Lang(
    val extension: String,
    val buildCommand: String?,
    val executeCommand: String,
    val isDefault: Boolean,
)