package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.swing.AutoLayout
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout

class TestcaseContent : BorderLayoutPanel(), Disposable {
    private val editorFactory = EditorFactory.getInstance()
    private val inputDoc = editorFactory.createDocument("")
    private val outputDoc = editorFactory.createDocument("")

    private val headerLabel = JBLabel("Testcase #1")
    private val headerActions = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_CONTENT,
        testcaseActionGroup() /* TODO: use actual actions */,
        true
    )

    private var inputEditor = editorFactory.createEditor(inputDoc).apply { customizeEditor() }
    private var outputEditor = editorFactory.createEditor(outputDoc).apply { customizeEditor() }

    init {
        add(BorderLayoutPanel().apply {
            add(headerLabel)
            headerActions.targetComponent = this
            add(headerActions.component, BorderLayout.LINE_END)
        }, BorderLayout.PAGE_START)

        add(JBPanel<JBPanel<*>>(AutoLayout(autoFitMainLines = true, uniformCrossLength = true)).apply {
            add(inputEditor.component)
            add(outputEditor.component)
        })
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

    override fun dispose() {

    }

}