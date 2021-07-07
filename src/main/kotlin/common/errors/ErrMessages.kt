package common.errors

fun Err.presentableString(): String {
    return when (this) {
        is Err.InternalErr -> "This was not supposed to happen, please file an issue (https://github.com/Pushpavel/AutoCp/issues/new)\n"
        is Err.TesterErr.BuildErr -> "Building the Solution File Failed\n"
        is Err.TesterErr.SolutionFileErr -> "There were issues with the Solution File\n"
        is Err.TesterErr.Cancelled -> "Testing Execution Cancelled\n"
        is Err.TesterErr.RuntimeErr -> "Your solution crashed\n"
        is Err.TesterErr.TimeoutErr -> "Time Limit specified in the problem is exceeded\n"
    }
}