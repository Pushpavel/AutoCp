package ui.vvm.swingBinding

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import common.diff.DiffAdapter
import common.diff.MyersDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ui.vvm.swingModels.update

fun <T> CoroutineScope.bind(
    comboBox: ComboBox<T>,
    listSource: Flow<List<T>>,
    selectedIndex: MutableSharedFlow<Int>,
    adapter: DiffAdapter<T>
) {
    val model = CollectionComboBoxModel<T>()
    val diff = MyersDiff(adapter)

    // flow to model
    launch {
        listSource.collect {
            // remove empty item
            if (model.items.isNotEmpty() && model.items[0] == null)
                model.removeAll()

            model.update(it, diff)

            // select first if none selected
            if (it.isNotEmpty() && comboBox.selectedIndex == -1)
                comboBox.selectedIndex = 0


        }
    }

    comboBox.model = model
    bindSelectionIndex(comboBox, selectedIndex)

    launch {
        listSource.collect {
            // add empty item
            if (it.isEmpty()) {
                model.add(0, null)
                model.selectedItem = null
            }
        }
    }
}