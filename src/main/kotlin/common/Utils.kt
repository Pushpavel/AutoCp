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

fun <T> Collection<T>.isItemsEqual(other: Collection<T>): Boolean {
    if (other.size != this.size)
        return false
    val iter1 = this.iterator()
    val iter2 = other.iterator()

    while (iter1.hasNext()) {
        if (iter1.next() != iter2.next())
            return false
    }

    return true
}

fun <T> Collection<T>.isIndex(index: Int): Boolean {
    return index in 0 until size
}

fun <T> Collection<T>.isNotIndex(index: Int): Boolean {
    return index !in 0 until size
}