package ui.vvm.swingModels

import com.intellij.openapi.ui.ComboBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
fun ComboBox<*>.bindSelectionIndex(scope: CoroutineScope, selectionItem: MutableSharedFlow<Int>) {
    // model to flow
    addActionListener {
        scope.launch {
            selectionItem.emit(selectedIndex)
        }
    }

    // flow to model
    scope.launch {
        selectionItem.collect {
            if (it != selectedIndex)
                selectedIndex = it
        }
    }
}