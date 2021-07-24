package ui.helpers

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Alternative to MainScope to avoid
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1300
 */
fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() })
}

fun childScope(parentScope: CoroutineScope?, context: CoroutineContext = Dispatchers.Default): CoroutineScope {
    val scope = CoroutineScope(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    } + context)

    parentScope?.launch {
        try {
            awaitCancellation()
        } finally {
            scope.cancel()
        }
    }

    return scope
}