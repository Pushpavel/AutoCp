package com.github.pushpavel.autocp.build

data class Lang(
    val extension: String,
    val buildCommand: String?,
    val executeCommand: String,
    val lineCommentPrefix: String,
    val isDefault: Boolean,
)

data class DefaultLangData(
    val extension: String,
    val commands: List<Pair<String?, String>>,
    val lineCommentPrefix: String
)