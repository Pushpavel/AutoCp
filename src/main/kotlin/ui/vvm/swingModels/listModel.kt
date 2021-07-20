package ui.vvm.swingModels

import com.intellij.ui.CollectionListModel
import common.diff.DeltaType
import common.diff.DiffAdapter
import common.diff.MyersDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.helpers.SimpleListDataListener

fun <T> Flow<List<T>>.toCollectionListModel(
    scope: CoroutineScope,
    sink: MutableSharedFlow<List<T>>,
    adapter: DiffAdapter<T>
): CollectionListModel<T> {
    val model = CollectionListModel<T>()
    val diff = MyersDiff(adapter)

    // for batch update
    var pauseFlow = false

    // flow to model
    scope.launch {
        collect {
            pauseFlow = true
            model.update(it, diff)
            pauseFlow = false
        }
    }

    // model to sink
    model.addListDataListener(SimpleListDataListener {
        if (!pauseFlow)
            scope.launch { sink.emit(model.items) }
    })



    return model
}


fun <T> CollectionListModel<T>.update(list: List<T>, diff: MyersDiff<T>) {
    val deltas = diff.compute(items, list)
    for (delta in deltas) {
        when (delta.type) {
            DeltaType.INSERT -> addAll(delta.x, list.subList(delta.y, delta.y + delta.length))
            DeltaType.DELETE -> removeRange(delta.x, delta.x + delta.length - 1)
            DeltaType.UPDATE -> {
                var y = delta.y
                for (x in delta.x until (delta.x + delta.length)) {
                    setElementAt(list[y]!!, x)
                    y++
                }
            }
            DeltaType.NULL -> {
                // do nothing
            }
        }
    }
}