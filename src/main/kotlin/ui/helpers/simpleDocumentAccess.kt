package ui.helpers

import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document
import javax.swing.text.PlainDocument


fun PlainDocument.setText(text: String) {
    this.replace(0, this.length, text, null)
}

fun PlainDocument.getText(): String {
    return this.getText(0, this.length)
}


fun Document.onChange(action: () -> Unit) {
    this.addDocumentListener(object : DocumentListener {
        override fun insertUpdate(p0: DocumentEvent?) = action()

        override fun removeUpdate(p0: DocumentEvent?) = action()

        override fun changedUpdate(p0: DocumentEvent?) {}
    })
}
