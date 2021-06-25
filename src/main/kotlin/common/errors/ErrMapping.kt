package common.errors

import kotlinx.coroutines.CancellationException

fun Throwable.mapToErr(): Err {
    return when (this) {
        is Err -> this
        is CancellationException -> Err.TesterErr.Cancelled
        else -> Err.InternalErr(this.stackTraceToString())
    }
}