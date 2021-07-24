package ui.vvm.swingModels

import com.intellij.ui.CollectionListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.SimpleListDataListener
import ui.vvm.swingBinding.update

fun <T> CoroutineScope.collectionListModel(
    source: Flow<List<T>>,
    sink: MutableSharedFlow<List<T>>,
): CollectionListModel<T> {
    val model = CollectionListModel<T>()

    // for batch update
    var pauseFlow = false

    // flow to model
    launch {
        source.collect {
            pauseFlow = true
            model.update(it)
            pauseFlow = false
        }
    }

    // model to sink
    model.addListDataListener(SimpleListDataListener {
        if (!pauseFlow)
            launch {
                sink.emit(model.items)
            }
    })

    return model
}