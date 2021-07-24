package ui.vvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ui.helpers.childScope

open class ViewModel(parentScope: CoroutineScope?) {
    val scope = childScope(parentScope, Dispatchers.Default)
}

