package ui.views

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ScopedView : View {
    val viewScope: CoroutineScope

    fun scope(action: suspend () -> Any) {
        viewScope.launch {
            action()
        }
    }
}