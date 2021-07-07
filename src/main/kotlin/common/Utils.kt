package common

import kotlinx.coroutines.Deferred

suspend fun <T> Deferred<T>.awaitAsResult() = runCatching {
    this.await()
}
