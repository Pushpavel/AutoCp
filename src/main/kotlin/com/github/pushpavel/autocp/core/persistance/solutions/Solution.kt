package com.github.pushpavel.autocp.core.persistance.solutions

data class Solution(
    val displayName: String,
    val pathString: String,
    val timeLimit: Int = 1000,
)