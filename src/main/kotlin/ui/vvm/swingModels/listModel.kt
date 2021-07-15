package ui.vvm.swingModels

import com.intellij.ui.CollectionListModel
import common.isItemsEqual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.SimpleListDataListener

fun <T> Flow<List<T>>.toCollectionListModel(
    scope: CoroutineScope,
    sink: MutableSharedFlow<List<T>>
): CollectionListModel<T> {
    val model = CollectionListModel<T>()

    // for batch update
    var pauseFlow = false

    // flow to model
    scope.launch {
        collect {
            if (!it.isItemsEqual(model.items)) {
                pauseFlow = true
                model.replaceAll(it)
                pauseFlow = false
            }
        }
    }

    // model to sink
    model.addListDataListener(SimpleListDataListener {
        if (!pauseFlow)
            sink.tryEmit(model.items)
    })



    return model
}