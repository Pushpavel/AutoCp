package common.errors

sealed class Err(message: String) : Exception(message) {
    class InternalErr(message: String) : Err(message)
}