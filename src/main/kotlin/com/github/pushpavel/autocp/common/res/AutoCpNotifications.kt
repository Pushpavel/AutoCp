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
                    "\t2.you clicked competitive companion button before AutoCp started listening. you can try again.\n" +
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

    const val velocityNullPointerMsg = "AutoCp does not have enough info on the issue. \n" +
            "You may have missed arguments for a file template directive like #parse directive without arguments\n" +
            "You may have not escaped a file template directive like #define which is also a valid c++ syntax\n\n" +
            "You can <a href=\"https://velocity.apache.org/engine/2.0/vtl-reference.html\" >refer</a> more about velocity file template syntax\n" +
            "Or if you do not use any file template specific syntax, simply wrap the entire file template within #[[template]]#"

    fun noConfigInContext() = notifyErr(
        "Can't run file with with AutoCp",
        "These may be the reasons:\n" +
                "this file is not enabled with AutoCp. open View > ToolWindows > AutoCp to do so.\n" +
                "Or this file's extension is not configured with AutoCp. go to Settings/Preferences > Tools > AutoCp > Languages to do so."
    )
}