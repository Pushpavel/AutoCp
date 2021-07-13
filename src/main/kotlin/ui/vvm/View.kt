package ui.vvm

import kotlinx.coroutines.*

interface View<T> {

    fun CoroutineScope.onViewModelBind(viewModel: T)

    fun bindToViewModel(scope: CoroutineScope, viewModel: T) = scope.launch {
        supervisorScope {
            onViewModelBind(viewModel)
        }
    }


}

fun <T> CoroutineScope.bind(view: View<T>, viewModel: T): Job {
    return view.bindToViewModel(this, viewModel)
}