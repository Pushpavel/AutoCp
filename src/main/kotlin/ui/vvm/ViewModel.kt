package ui.vvm

import com.intellij.openapi.Disposable
import kotlinx.coroutines.*

open class ViewModel : Disposable {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob() + CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
    })

    override fun dispose() = scope.cancel()
}