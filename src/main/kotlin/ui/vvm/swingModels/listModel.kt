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
            val deltas = diff.compute(model.items, it)
            for (delta in deltas) {
                when (delta.type) {
                    DeltaType.INSERT -> model.addAll(delta.x, it.subList(delta.y, delta.y + delta.length))
                    DeltaType.DELETE -> model.removeRange(delta.x, delta.x + delta.length - 1)
                    DeltaType.UPDATE -> {
                        var y = delta.y
                        for (x in delta.x until (delta.x + delta.length)) {
                            model.setElementAt(it[y]!!, x)
                            y++
                        }
                    }
                    DeltaType.NULL -> {
                        // do nothing
                    }
                }
            }
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