package ui.vvm.swingModels

import com.intellij.ui.CollectionComboBoxModel
import common.diff.DiffAdapter
import common.diff.MyersDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> CoroutineScope.collectionComboBoxModel(
    source: Flow<List<T>>,
    adapter: DiffAdapter<T>
): CollectionComboBoxModel<T> {
    val model = CollectionComboBoxModel<T>()
    val diff = MyersDiff(adapter)

    // flow to model
    launch {
        source.collect {
            model.update(it, diff)
        }
    }

    return model
}
