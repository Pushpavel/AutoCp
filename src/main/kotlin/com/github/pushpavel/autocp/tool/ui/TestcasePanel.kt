package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.github.pushpavel.autocp.database.models.Testcase
import com.github.pushpavel.autocp.common.ui.swing.editableList.ListItemView
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import javax.swing.BorderFactory

class TestcasePanel(val model: CollectionListModel<Testcase>) : ListItemView<Testcase> {

    private var testcase: Testcase? = null
    private val editorFactory = EditorFactory.getInstance()

    private val inputDoc = editorFactory.createDocument("")
    private val outputDoc = editorFactory.createDocument("")

    private val titleLabel = JBLabel("")
    private val inputEditor = editorFactory.createEditor(inputDoc).apply { customizeEditor() }
    private val outputEditor = editorFactory.createEditor(outputDoc).apply { customizeEditor() }

    override val component: DialogPanel = panel {
        row {
            inputEditor.headerComponent = testcaseHeader(model, titleLabel, ::testcaseIndex)
            outputEditor.headerComponent = inputEditor.component
            cell(outputEditor.component).align(AlignX.FILL)
        }
    }.withBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4))


    override fun contentChanged(item: Testcase) {
        runUndoTransparentWriteAction {
            titleLabel.text = item.name

            if (item.input != inputDoc.text)
                inputDoc.setText(item.input)
            if (item.output != outputDoc.text)
                outputDoc.setText(item.output)
        }
        testcase = item
    }

    init {
        inputDoc.updateModelOnChange { copy(input = it) }
        outputDoc.updateModelOnChange { copy(output = it) }
    }

    private fun Editor.customizeEditor() {
        settings.apply {
            additionalLinesCount = 1
            isLineMarkerAreaShown = false
        }
    }

    private fun Document.updateModelOnChange(predicate: Testcase.(String) -> Testcase) {
        addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                if (testcase == null) return
                val index = testcaseIndex() ?: return
                val updatedTestcase = model.items[index].predicate(event.document.text)
                model.setElementAt(updatedTestcase, index)
            }
        }, this@TestcasePanel)
    }

    private fun testcaseIndex(): Int? {
        return model.items.indexOfFirst { it.name == testcase?.name }.takeIf { it != -1 }
    }

    override fun dispose() {
        editorFactory.releaseEditor(inputEditor)
        editorFactory.releaseEditor(outputEditor)
    }

}