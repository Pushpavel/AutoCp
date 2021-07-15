package ui.vvm

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import kotlinx.coroutines.*

open class ViewModel : Disposable {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob() + CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
    })


    override fun dispose() = scope.cancel()

    fun <T : ViewModel> T.withParent(parent: Disposable): T {
        Disposer.register(parent, this)
        return this
    }
}

