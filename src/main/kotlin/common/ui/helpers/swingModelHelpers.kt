package common.ui.helpers

import javax.swing.JComboBox
import javax.swing.JList

fun <T> JList<T>.onSelectedValue(action: JList<T>.(T?) -> Unit) {
    var prevValue: T? = null
    action(selectedValue)
    addListSelectionListener {
        if (prevValue != selectedValue)
            action(selectedValue)
        prevValue = selectedValue
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> JComboBox<T>.onSelectedItem(action: JComboBox<T>.(T?) -> Unit) {
    var prevValue: T? = null
    action(selectedItem as T?)
    addActionListener {
        if (prevValue != selectedItem)
            action(selectedItem as T?)
        prevValue = selectedItem as T?
    }
}