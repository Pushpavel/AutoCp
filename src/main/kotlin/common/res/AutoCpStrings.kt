package common.res

object AutoCpStrings {
    const val runConfigName = "AutoCp Solution"
    const val runConfigId = "AutoCp"
    const val runConfigDescription = "Test your Competitive Programming solution with AutoCp"


    // Error messages
    const val problemGatheringTitle = "Problem Gathering"

    const val fileIssue = "please file an issue (https://github.com/Pushpavel/AutoCp/issues/new/choose)"

    fun portTakenMsg(port: Int) = "Port $port is already in use."

    fun portRetryMsg(port: Int) = "Retrying with port $port..."

    fun allPortFailedMsg() = "Could not find a free port to use with competitive companion. " +
            "You could be running multiple instance of AutoCp installed IDEs or any other tools that uses competitive companion. " +
            "Close other programs or restart your pc. If this issue still occurs, " + fileIssue
}

fun String.failed(): String = "$this Failed"