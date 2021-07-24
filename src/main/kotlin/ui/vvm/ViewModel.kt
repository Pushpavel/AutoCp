package ui.vvm

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import kotlinx.coroutines.*

open class ViewModel(parentDisposable: Disposable?) : Disposable {

    val scope by lazy {
        if (parentDisposable != null)
            Disposer.register(parentDisposable, this)

        CoroutineScope(Dispatchers.Default + SupervisorJob() + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
        })
    }

    override fun dispose() = scope.cancel()
}

