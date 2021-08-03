package ui.dsl

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import javax.swing.ListCellRenderer

fun <T> Cell.comboBoxView(
    model: CollectionComboBoxModel<T>,
    getter: () -> T?,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): CellBuilder<ComboBox<T>> {
    return comboBox(model, { getter() ?: model.items.firstOrNull() }, setter, renderer)
}

fun <T> Cell.simpleComboBoxView(
    list: List<T>,
    getter: () -> T?,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): CellBuilder<ComboBox<T>> {
    val model = CollectionComboBoxModel(list)
    return comboBox(model, { getter() ?: model.items.firstOrNull() }, setter, renderer)
}

