package settings

import plugin.settings.SolutionLanguage
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document
import javax.swing.text.PlainDocument

class LanguageItemModel {
    val nameDoc = PlainDocument()
    val extensionDoc = PlainDocument()
    val buildCommandDoc = PlainDocument()

    var changeByUserListener: ((lang: SolutionLanguage?) -> Unit)? = null
    private var langId: Long? = null
    private var notify = true

    init {
        val notifier = {
            if (notify)
                changeByUserListener?.let { it(createValue()) }
            Unit
        }
        nameDoc.onChange(notifier)
        extensionDoc.onChange(notifier)
        buildCommandDoc.onChange(notifier)
    }

    fun update(item: SolutionLanguage?) {
        val name = item?.name ?: ""
        val extension = item?.extension ?: ""
        val buildCommand = item?.buildCommand ?: ""
        langId = item?.id

        notify = false
        if (name != nameDoc.getText())
            nameDoc.setText(name)
        if (extension != extensionDoc.getText())
            extensionDoc.setText(extension)
        if (buildCommand != buildCommandDoc.getText())
            buildCommandDoc.setText(buildCommand)
        notify = true
    }

    private fun createValue(): SolutionLanguage? {
        val name = nameDoc.getText()
        val extension = extensionDoc.getText()
        val buildCommand = buildCommandDoc.getText()

        return langId?.let { SolutionLanguage(name, extension, buildCommand, it) }
    }


    // UTILITY

    private fun PlainDocument.setText(text: String) {
        this.replace(0, this.length, text, null)
    }

    private fun PlainDocument.getText(): String {
        return this.getText(0, this.length)
    }

    private fun Document.onChange(action: () -> Unit) {
        this.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(p0: DocumentEvent?) = action()

            override fun removeUpdate(p0: DocumentEvent?) = action()

            override fun changedUpdate(p0: DocumentEvent?) {}
        })
    }
}