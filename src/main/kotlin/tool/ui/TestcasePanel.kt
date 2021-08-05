package tool.ui

import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import database.models.Testcase
import ui.swing.editableList.ListItemView
import javax.swing.BorderFactory

class TestcasePanel(val model: CollectionListModel<Testcase>) : ListItemView<Testcase> {

    private var testcase: Testcase? = null
    private val editorFactory = EditorFactory.getInstance()

    private val inputDoc = editorFactory.createDocument("")
    private val outputDoc = editorFactory.createDocument("")

    private val inputEditor = editorFactory.createEditor(inputDoc).apply { customizeEditor() }
    private val outputEditor = editorFactory.createEditor(outputDoc).apply { customizeEditor() }

    override val component = panel(LCFlags.fillX) {
        row {
            inputEditor.headerComponent = JBLabel("Testcase #1").withBorder(BorderFactory.createEmptyBorder(0, 4, 4, 0))
            outputEditor.headerComponent = inputEditor.component
            outputEditor.component(CCFlags.growX)
        }
    }

    override fun contentChanged(item: Testcase) {
        runUndoTransparentWriteAction {

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
        }
    }

    private fun Document.updateModelOnChange(predicate: Testcase.(String) -> Testcase) {
        addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                if (testcase == null)
                    return
                val index = model.items.indexOfFirst { it.name == testcase?.name }.takeIf { it != -1 } ?: return
                val updatedTestcase = model.items[index].predicate(event.document.text)
                model.setElementAt(updatedTestcase, index)
            }
        }, this@TestcasePanel)
    }

    override fun dispose() {
        editorFactory.releaseEditor(inputEditor)
        editorFactory.releaseEditor(outputEditor)
    }

}