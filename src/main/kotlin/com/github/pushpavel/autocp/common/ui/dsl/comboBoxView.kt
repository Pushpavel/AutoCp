package com.github.pushpavel.autocp.common.ui.dsl

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.github.pushpavel.autocp.common.ui.helpers.SimpleListDataListener
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindItem
import javax.swing.ListCellRenderer
import com.intellij.ui.dsl.builder.Cell

fun <T> Row.comboBoxView(
    model: CollectionComboBoxModel<T>,
    getSelectionPredicate: (T) -> Boolean,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): Cell<ComboBox<T>> {
    return comboBox(model, renderer).bindItem({
        model.items.run {
            firstOrNull { getSelectionPredicate(it) } ?: firstOrNull()
        }
    }, setter).applyToComponent {
        model.addListDataListener(SimpleListDataListener {
            if (model.items.isNotEmpty()) {
                if (selectedItem == null)
                    selectedItem = model.items.run {
                        firstOrNull { getSelectionPredicate(it) } ?: firstOrNull()
                    }
            } else if (selectedItem != null)
                selectedItem = null

        })
    }
}

fun <T> Row.simpleComboBoxView(
    list: List<T>,
    getSelectionPredicate: (T) -> Boolean,
    setter: (T?) -> Unit,
    renderer: ListCellRenderer<T?>? = null,
): Cell<ComboBox<T>> {
    val model = CollectionComboBoxModel(list)
    return comboBoxView(model, getSelectionPredicate, setter, renderer)
}

