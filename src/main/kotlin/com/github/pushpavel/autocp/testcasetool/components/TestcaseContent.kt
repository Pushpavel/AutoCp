package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.swing.AutoLayout
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcase
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout

class TestcaseContent(private val model: CollectionListModel<Testcase>) : BorderLayoutPanel(), Disposable {
    private val editorFactory = EditorFactory.getInstance()
    private val inputDoc = editorFactory.createDocument("")
    private val outputDoc = editorFactory.createDocument("")
    private val headerLabel = JBLabel("Testcase #")

    private val headerActions = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_CONTENT,
        DefaultActionGroup().apply {
            val deleteAction = object :
                AnAction("Delete Testcase", "Deletes the Testcase in Testcase viewer", AllIcons.General.Remove) {
                override fun actionPerformed(e: AnActionEvent) {
                    if (currentIndex != -1)
                        model.remove(currentIndex)
                }
            }
            add(deleteAction)
        },
        true
    )

    private var currentIndex = -1

    private var inputEditor = editorFactory.createEditor(inputDoc).apply { customizeEditor() }
    private var outputEditor = editorFactory.createEditor(outputDoc).apply { customizeEditor() }

    fun update(index: Int, testcase: Testcase) {
        headerLabel.text = "Testcase #${testcase.num}"
        currentIndex = index
        runWriteAction {
            if (testcase.input != inputDoc.text)
                inputDoc.setText(testcase.input)
            if (testcase.output != outputDoc.text)
                outputDoc.setText(testcase.output)
        }
    }

    init {
        add(BorderLayoutPanel().apply {
            add(headerLabel)
            headerActions.setTargetComponent(this)
            add(headerActions.component, BorderLayout.LINE_END)
        }, BorderLayout.PAGE_START)

        add(JBPanel<JBPanel<*>>(AutoLayout(autoFillMainLines = true, uniformCrossLength = true)).apply {
            add(inputEditor.component)
            add(outputEditor.component)
        })

        val docListener = object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                if (currentIndex != -1) {
                    val testcase = model.getElementAt(currentIndex)
                    model.setElementAt(testcase.copy(input = inputDoc.text, output = outputDoc.text), currentIndex)
                }
            }
        }

        inputDoc.addDocumentListener(docListener)
        outputDoc.addDocumentListener(docListener)
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
        editorFactory.releaseEditor(inputEditor)
        editorFactory.releaseEditor(outputEditor)
        currentIndex = -1
    }

}