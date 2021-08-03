package ui.dsl

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import javax.swing.ListCellRenderer

fun <T> Cell.comboBoxView(
    model: CollectionComboBoxModel<T>,
    getSelectionPredicate: (T) -> Boolean,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): CellBuilder<ComboBox<T>> {
    return comboBox(model, {
        model.items.run {
            firstOrNull { getSelectionPredicate(it) } ?: firstOrNull()
        }
    }, setter, renderer)
}

fun <T> Cell.simpleComboBoxView(
    list: List<T>,
    getSelectionPredicate: (T) -> Boolean,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): CellBuilder<ComboBox<T>> {
    val model = CollectionComboBoxModel(list)
    return comboBoxView(model, getSelectionPredicate, setter, renderer)
}

