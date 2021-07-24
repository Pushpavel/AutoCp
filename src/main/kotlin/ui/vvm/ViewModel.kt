package ui.vvm

import kotlinx.coroutines.CoroutineScope
import ui.helpers.childScope

open class ViewModel(parentScope: CoroutineScope?) {
    val scope = childScope(parentScope)
}

