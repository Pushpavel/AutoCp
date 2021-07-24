package ui.helpers

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

val exceptionPrinter = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

/**
 * Alternative to MainScope to avoid
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1300
 */
fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + exceptionPrinter)
}

fun viewScope(parentScope: CoroutineScope?) = childScope(parentScope, Dispatchers.Main)

fun childScope(parentScope: CoroutineScope?, context: CoroutineContext): CoroutineScope {
    val scope = CoroutineScope(exceptionPrinter + SupervisorJob() + context)

    parentScope?.launch {
        try {
            awaitCancellation()
        } finally {
            scope.cancel()
        }
    }

    return scope
}