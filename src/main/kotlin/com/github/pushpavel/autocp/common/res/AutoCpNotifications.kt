package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.common.helpers.notifyErr
import com.github.pushpavel.autocp.common.helpers.notifyWarn
import com.github.pushpavel.autocp.gather.base.ProblemGatheringErr
import java.net.SocketException

object AutoCpNotifications {
    fun problemGatheringErr(e: ProblemGatheringErr) = when (e) {
        is ProblemGatheringErr.AllPortsTakenErr -> notifyErr(
            "Connection with competitive companion failed",
            "All ports supported by competitive companion browser extension is busy\n" +
                    "Ports:\n" + e.ports.joinToString("\n") { it.toString() }
        )
    }

    fun problemGatheringUncaught(e: Exception) = notifyWarn(
        "Stopped listening to competitive companion",
        when (e) {
            is SocketException -> "cancelled"
            else -> R.strings.defaultFileIssue(e)
        }
    )
}