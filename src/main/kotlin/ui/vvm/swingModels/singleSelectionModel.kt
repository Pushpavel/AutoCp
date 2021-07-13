package ui.vvm.swingModels

import com.intellij.ui.SingleSelectionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

fun MutableSharedFlow<Int>.toSingleSelectionModel(scope: CoroutineScope): SingleSelectionModel {

    val model = SingleSelectionModel()

    var previousIndex = model.minSelectionIndex

    // model to flow
    model.addListSelectionListener {
        if (model.minSelectionIndex != previousIndex)
            tryEmit(model.minSelectionIndex)

        previousIndex = model.minSelectionIndex
    }

    // flow to model
    scope.launch {
        collect {
            model.setSelectionInterval(it, it)
        }
    }

    return model
}