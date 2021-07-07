package common.errors

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException

fun Throwable.mapToErr(): Err {
    return when (this) {
        is Err -> this
        is TimeoutCancellationException -> Err.TesterErr.TimeoutErr
        is CancellationException -> Err.TesterErr.Cancelled
        else -> Err.InternalErr(this.stackTraceToString())
    }
}