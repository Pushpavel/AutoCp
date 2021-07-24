package ui.vvm.swingModels

import com.intellij.ui.SingleSelectionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun CoroutineScope.singleSelectionModel(flow: MutableSharedFlow<Int>): SingleSelectionModel {
    return singleSelectionModel(flow, flow)
}

fun CoroutineScope.singleSelectionModel(source: Flow<Int>, sink: MutableSharedFlow<Int>): SingleSelectionModel {

    val model = SingleSelectionModel()

    var previousIndex = model.minSelectionIndex

    // flow to model
    launch {
        source.collect {
            model.setSelectionInterval(it, it)
        }
    }

    // model to sink
    model.addListSelectionListener {
        if (model.minSelectionIndex != previousIndex)
            launch { sink.emit(model.minSelectionIndex) }

        previousIndex = model.minSelectionIndex
    }

    return model
}