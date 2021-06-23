package common.errors

/** Testing Process Result type  */

sealed interface TpResult<out T> : Result<T>

sealed class TpErr(error: String) : Err(error), TpResult<Nothing> {

    class InternalErr(error: String) : TpErr(error)

}
