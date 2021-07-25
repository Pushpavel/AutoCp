package ui.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <U, D> MutableStateFlow<U>.biState(
    scope: CoroutineScope,
    initialValue: D,
    downStream: (U) -> D,
    upstream: U.(D) -> U
): MutableStateFlow<D> {
    val state = MutableStateFlow(initialValue)
    var pause = false

    scope.launch {
        collect {
            pause = true
            state.emit(downStream(it))
        }
    }

    scope.launch {
        state.collect {
            if (!pause)
                emit(value.upstream(it))
            pause = false
        }
    }

    return state
}