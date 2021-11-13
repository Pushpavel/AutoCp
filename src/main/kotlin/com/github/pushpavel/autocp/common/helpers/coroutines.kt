package com.github.pushpavel.autocp.common.helpers

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import kotlinx.coroutines.*

val defaultExceptionHandler = CoroutineExceptionHandler { _, t -> t.printStackTrace() }

fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + SupervisorJob() + defaultExceptionHandler)
}

fun defaultScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default + SupervisorJob() + defaultExceptionHandler)
}

fun ioScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.IO + SupervisorJob() + defaultExceptionHandler)
}

/**
 * Cancels [CoroutineScope] on [parent] disposal.
 */
fun CoroutineScope.cancelBy(parent: Disposable): CoroutineScope {
    Disposer.register(parent) { cancel() }
    return this
}