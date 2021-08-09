package common.ui.helpers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val exceptionPrinter = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

/**
 * Alternative to MainScope to avoid
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1300
 */
@Deprecated("don't use it")
fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + exceptionPrinter)
}