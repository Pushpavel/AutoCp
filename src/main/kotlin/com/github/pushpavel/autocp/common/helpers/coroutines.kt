package com.github.pushpavel.autocp.common.helpers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.swing.Swing

val defaultExceptionHandler = CoroutineExceptionHandler { _, t -> t.printStackTrace() }

fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Swing + SupervisorJob() + defaultExceptionHandler)
}

fun defaultScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default + SupervisorJob() + defaultExceptionHandler)
}

fun ioScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.IO + SupervisorJob() + defaultExceptionHandler)
}