package tool

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import database.models.Testcase

/**
 * ViewModel for [TestcaseUI]
 */
class TestcaseModel {
    val inputDoc = EditorFactory.getInstance().createDocument("")
    val outputDoc = EditorFactory.getInstance().createDocument("")

    private var currentTestcase: Testcase? = null
    var changeByUserListener: ((testcase: Testcase) -> Unit)? = null
    private var notify = true

    init {
        val notifier = {
            val value = createValue()
            if (notify && value != null)
                changeByUserListener?.let { it(value) }
            Unit
        }
        inputDoc.onChange(notifier)
        outputDoc.onChange(notifier)
    }


    fun update(item: Testcase?) {
        currentTestcase = item
        val input = item?.input ?: ""
        val output = item?.output ?: ""

        runWriteAction {
            notify = false
            if (input != inputDoc.text)
                inputDoc.setText(input)
            if (output != outputDoc.text)
                outputDoc.setText(output)
            notify = true
        }
    }

    fun createValue(): Testcase? {
        val item = currentTestcase ?: return null
        val input = inputDoc.text
        val output = outputDoc.text
        return Testcase(item.name, input, output)
    }

    // UTILITIES
    private fun Document.onChange(action: () -> Unit) {
        this.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) = action()
        })
    }
}