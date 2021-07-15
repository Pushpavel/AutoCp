package ui.vvm.swingModels

import com.intellij.ui.SingleSelectionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun Flow<Int>.toSingleSelectionModel(scope: CoroutineScope, sink: MutableStateFlow<Int>): SingleSelectionModel {

    val model = SingleSelectionModel()

    var previousIndex = model.minSelectionIndex

    // flow to model
    scope.launch {
        collect {
            model.setSelectionInterval(it, it)
        }
    }

    // model to sink
    model.addListSelectionListener {
        if (model.minSelectionIndex != previousIndex)
            sink.tryEmit(model.minSelectionIndex)

        previousIndex = model.minSelectionIndex
    }

    return model
}