package common.res

import database.models.Problem


object AutoCpStrings {
    const val runConfigName = "AutoCp Solution"
    const val runConfigId = "AutoCp"
    const val runConfigDescription = "Test your Competitive Programming solution with AutoCp"


    // Error messages
    const val problemGatheringTitle = "Problem Gathering"

    const val fileIssue = "please file an issue (https://github.com/Pushpavel/AutoCp/issues/new/choose)"


    // Server Port Errors
    fun portTakenMsg(port: Int) = "Port $port is already in use."

    fun portRetryMsg(port: Int) = "Retrying with port $port..."

    fun allPortFailedMsg() = "" +
            "Could not find a free port to use with competitive companion. " +
            "You may be running multiple instances of AutoCp installed IDEs or other tools that use competitive companion. " +
            "Try closing other programs or restarting your pc. If this issue still occurs, " + fileIssue


    const val competitiveCompanionJsonFormatErrMsg =
        "The Problem sent by competitive companion was not parsed correctly. This was not supposed to happen, $fileIssue"

    fun incompleteProblemsGathering(group: String, parsed: List<Problem>, total: Int): String {
        val c = total - parsed.size
        return "$c of the problem${if (c == 1) "" else 's'} from $group are not gathered. " +
                "Possibly you clicked competitive companion button before AutoCp started listening. " +
                "If it was not the case, " + fileIssue + "\n\n" +
                gatheredProblemsList(parsed)
    }

    fun gatheredProblemsList(problems: List<Problem>): String {
        return "Gathered Problems:\n" +
                problems.joinToString("\n") { it.name }
    }
}

fun String.failed(): String = "$this Failed"