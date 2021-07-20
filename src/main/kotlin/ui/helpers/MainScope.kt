package ui.helpers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Alternative to MainScope to avoid
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1300
 */
fun mainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main + CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() })
}