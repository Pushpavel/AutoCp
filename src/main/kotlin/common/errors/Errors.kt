package common.errors

sealed class Err(message: String) : Exception(message) {
    class InternalErr(message: String) : Err(message)
    sealed class TesterErr(message: String) : Err(message) {
        class BuildErr(message: String) : TesterErr(message)
    }
}