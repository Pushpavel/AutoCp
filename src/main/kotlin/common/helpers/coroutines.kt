package common.helpers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val defaultExceptionHandler = CoroutineExceptionHandler { _, t -> t.printStackTrace() }

fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + defaultExceptionHandler)
}

fun defaultScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default + defaultExceptionHandler)
}