package ui.vvm.swingModels

import com.intellij.ui.CollectionListModel
import common.isItemsEqual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.SimpleListDataListener

fun <T> MutableSharedFlow<List<T>>.toCollectionListModel(scope: CoroutineScope): CollectionListModel<T> {
    val model = CollectionListModel<T>()

    // for batch update
    var pauseFlow = false

    // model to flow
    model.addListDataListener(SimpleListDataListener {
        if (!pauseFlow)
            this.tryEmit(model.items)
    })

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

    return model
}