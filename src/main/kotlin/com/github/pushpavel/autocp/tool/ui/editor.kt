package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener

fun Document.onChanged(parentDisposable: Disposable, listener: (String) -> Unit) {
    this.addDocumentListener(object: DocumentListener {
        override fun documentChanged(event: DocumentEvent) = listener(event.document.text)
    }, parentDisposable)
}

fun Editor.customizeEditor() {
    settings.apply {
        additionalLinesCount = 1
        isLineMarkerAreaShown = false
    }
}
