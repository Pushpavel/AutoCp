package common.ui.vvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import common.ui.helpers.childScope

open class ViewModel(parentScope: CoroutineScope?) {
    val scope = childScope(parentScope, Dispatchers.Default)
}

