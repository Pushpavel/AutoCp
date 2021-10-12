package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.common.helpers.notifyErr
import com.github.pushpavel.autocp.common.helpers.notifyWarn
import com.github.pushpavel.autocp.gather.base.ProblemGatheringErr
import com.intellij.util.IncorrectOperationException
import java.net.SocketException

object AutoCpNotifications {
    fun couldNotWriteToAutoCpFile() = notifyErr(
        "Could not write to .autocp file",
        "This may be caused by a file association change, ${R.strings.fileIssue}"
    )

    fun problemGatheringErr(e: ProblemGatheringErr) = when (e) {
        is ProblemGatheringErr.AllPortsTakenErr -> notifyErr(
            "Connection with competitive companion failed",
            "All ports supported by competitive companion browser extension is busy\n" +
                    "Ports:\n" + e.ports.joinToString("\n") { it.toString() }
        )
        ProblemGatheringErr.JsonErr -> notifyErr(
            "Generating File Failed",
            "The Problem sent by competitive companion was not parsed correctly. " +
                    "This was not supposed to happen, ${AutoCpStrings.fileIssue}"
        )
        ProblemGatheringErr.TimeoutErr -> notifyErr(
            "Generating File Failed",
            "Competitive companion has not responded for too long. You should try again.\n" +
                    "This could happen due to below reasons\n" +
                    "\t1.Competitive companion is shutdown (you may have closed the browser tab)\n" +
                    "\t2.you clicked competitive companion button before AutoCp started listening.\n" +
                    "if these were not the reason, ${AutoCpStrings.fileIssue}\n"
        )
        ProblemGatheringErr.Cancellation -> {
            // ignored
        }
    }

    fun problemGatheringUncaught(e: Exception) = notifyWarn(
        "Stopped listening to competitive companion",
        when (e) {
            is SocketException -> "cancelled"
            else -> R.strings.defaultFileIssue(e)
        }
    )

    fun fileGenerationIncorrectOperation(e: IncorrectOperationException) = notifyErr(
        "File could not be generated due to file template issue",
        e.localizedMessage + "\n\n" +
                "If the error above is unexpected, ${R.strings.fileIssue}"
    )
}