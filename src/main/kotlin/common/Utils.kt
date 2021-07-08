package common

import kotlinx.coroutines.Deferred

suspend fun <T> Deferred<T>.awaitAsResult() = runCatching {
    this.await()
}

fun Throwable.causes(): ArrayList<Throwable> {
    val list = ArrayList<Throwable>()
    var cause: Throwable? = this
    while (cause != null) {
        list.add(cause)
        cause = cause.cause
    }
    return list
}