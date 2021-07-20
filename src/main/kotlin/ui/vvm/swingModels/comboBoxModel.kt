package ui.vvm.swingModels

import com.intellij.ui.CollectionComboBoxModel
import common.diff.DiffAdapter
import common.diff.MyersDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<List<T>>.toCollectionComboBoxModel(
    scope: CoroutineScope,
    adapter: DiffAdapter<T>
): CollectionComboBoxModel<T> {
    val model = CollectionComboBoxModel<T>()
    val diff = MyersDiff(adapter)

    // flow to model
    scope.launch {
        collect {
            model.update(it, diff)
        }
    }

    return model
}
