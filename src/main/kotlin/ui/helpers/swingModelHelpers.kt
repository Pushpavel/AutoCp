package ui.helpers

import javax.swing.JList

fun <T> JList<T>.onSelectedValue(action: JList<T>.(T?) -> Unit) {
    var prevValue: T? = null
    addListSelectionListener {
        if (prevValue != selectedValue)
            action(selectedValue)
        prevValue = selectedValue
    }
}