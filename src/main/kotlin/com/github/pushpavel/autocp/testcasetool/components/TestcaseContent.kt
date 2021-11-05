package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.helpers.layoutUpdater
import com.github.pushpavel.autocp.common.ui.swing.AutoLayout
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.Component

class TestcaseContent : JBPanel<JBPanel<*>>() {
    private val editorFactory = EditorFactory.getInstance()
    private val inputDoc = editorFactory.createDocument("")
    private val outputDoc = editorFactory.createDocument("")

    init {
        layout = AutoLayout()
    }

    val header: Component by layoutUpdater(ActionToolBarLayout(
        testcaseActionGroup(),
        horizontal = true
    ).apply {
        content = JBLabel("Testcase $1")
        toolBarConstraint = BorderLayout.LINE_END
    }) {
        remove(it)
        add(it)
    }

    var inputEditor: Editor by layoutUpdater(editorFactory.createEditor(inputDoc).apply {
        customizeEditor()
    }) {
        remove(it.component)
        add(it.component)
    }
    var outputEditor: Editor by layoutUpdater(editorFactory.createEditor(outputDoc).apply {
        customizeEditor()
    }) {
        remove(it.component)
        add(it.component)
    }

    private fun Editor.customizeEditor() {
        settings.apply {
            additionalLinesCount = 1
            isLineMarkerAreaShown = false
        }
        document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                component.updateUI()
            }
        })
    }

}