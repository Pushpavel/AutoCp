package com.github.pushpavel.autocp.common.helpers

import kotlinx.coroutines.Deferred

suspend fun <T> Deferred<T>.awaitAsResult() = runCatching {
    this.await()
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