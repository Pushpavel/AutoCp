package common.ui.helpers

import com.intellij.ui.DocumentAdapter
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

fun Document.onChange(action: Document.(String) -> Unit) {
    addDocumentListener(object : DocumentAdapter() {
        override fun textChanged(e: DocumentEvent) {
            action(e.document.getText(0, e.document.length))
        }
    })
}