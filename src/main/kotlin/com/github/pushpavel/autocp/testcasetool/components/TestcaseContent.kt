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
    private lateinit var body: JBPanel<JBPanel<*>>
    private val headerActions = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_CONTENT,
        DefaultActionGroup().apply {
            val deleteAction = object :
                AnAction("Delete Testcase", "Deletes the Testcase in Testcase viewer", AllIcons.Actions.GC) {
                override fun actionPerformed(e: AnActionEvent) {
                    if (testcaseNum != -1) {
                        val index = model.items.indexOfFirst { it.num == testcaseNum }
                        if (index != -1)
                            model.remove(index)
                    }
                }
            }


            val expandAction: AnAction?
            var collapseAction: AnAction? = null

            expandAction = object :
                AnAction("Expand Testcase", "Expands the Testcase in Testcase viewer", AllIcons.General.Add) {
                override fun actionPerformed(e: AnActionEvent) {
                    add(body)
                    add(collapseAction!!)
                    remove(this)
                    updateUI()
                }
            }

            collapseAction = object : AnAction(
                "Collapse Testcase",
                "Collapses the Testcase in Testcase viewer",
                AllIcons.General.HideToolWindow
            ) {
                override fun actionPerformed(e: AnActionEvent) {
                    remove(body)
                    add(expandAction)
                    remove(this)
                    updateUI()
                }
            }

            add(deleteAction)
            add(collapseAction)
        },
        true
    )

    private var testcaseNum = -1

    private var inputEditor = editorFactory.createEditor(inputDoc).apply { customizeEditor() }
    private var outputEditor = editorFactory.createEditor(outputDoc).apply { customizeEditor() }

    fun update(testcase: Testcase) {
        headerLabel.text = "Testcase #${testcase.num}"
        testcaseNum = testcase.num
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

        val docListener = object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                if (testcaseNum != -1) {
                    val index = model.items.indexOfFirst { it.num == testcaseNum }
                    if (index != -1) {
                        val testcase = model.getElementAt(index)
                        model.setElementAt(testcase.copy(input = inputDoc.text, output = outputDoc.text), index)
                    }
                }
            }
        }

        body = JBPanel<JBPanel<*>>(AutoLayout(autoFillMainLines = true, uniformCrossLength = true)).apply {
            add(inputEditor.component)
            add(outputEditor.component)
        }

        add(body)

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
        testcaseNum = -1
    }

}