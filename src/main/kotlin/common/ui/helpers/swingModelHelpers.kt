package common.ui.helpers

import com.intellij.ui.DocumentAdapter
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.event.DocumentEvent
import javax.swing.text.Document

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

fun Document.onChange(action: Document.(String) -> Unit) {
    addDocumentListener(object : DocumentAdapter() {
        override fun textChanged(e: DocumentEvent) {
            action(e.document.getText(0, e.document.length))
        }
    })
}