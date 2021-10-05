package com.github.pushpavel.autocp.gather.base

sealed class ProblemGatheringErr : Exception() {
    data class AllPortsTakenErr(val ports: List<Int>) : ProblemGatheringErr()
    object JsonErr : ProblemGatheringErr()
    object TimeoutErr : ProblemGatheringErr()
}