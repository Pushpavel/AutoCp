package com.github.pushpavel.autocp.common.helpers

import com.github.pushpavel.autocp.common.errors.NoReachErr
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Deferred
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.pathString

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

suspend inline fun <T> T.catchAndLog(crossinline action: suspend T.() -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Path.relativeTo(project: Project): Path {
    return if (isAbsolute) {
        val basePath = project.basePath ?: throw NoReachErr
        Path(basePath).relativize(this)
    } else
        this
}

fun Path.absoluteFrom(project: Project): Path {
    return if (!isAbsolute) {
        val basePath = project.basePath ?: throw NoReachErr
        Paths.get(Path(basePath).pathString, this.pathString)
    } else
        this
}