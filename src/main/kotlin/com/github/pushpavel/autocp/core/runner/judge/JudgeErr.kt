package com.github.pushpavel.autocp.core.runner.judge

import com.github.pushpavel.autocp.core.persistance.solutions.Solution

sealed class JudgeErr : Exception() {
    data class NoSolution(val solutionPathString: String) : JudgeErr()
    data class NoTestcases(val solution: Solution, val solutionPathString: String) : JudgeErr()
}