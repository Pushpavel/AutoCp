package ui.vvm.swingBinding

import com.intellij.openapi.ui.ComboBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun CoroutineScope.bindSelectionIndex(comboBox: ComboBox<*>, selectionItem: MutableSharedFlow<Int>) {
    // model to flow
    comboBox.addActionListener {
        launch {
            selectionItem.emit(comboBox.selectedIndex)
        }
    }

    // flow to model
    launch {
        selectionItem.collect {
            if (it != comboBox.selectedIndex)
                comboBox.selectedIndex = it
        }
    }
}