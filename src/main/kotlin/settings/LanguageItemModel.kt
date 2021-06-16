package settings

import plugin.settings.SolutionLanguage
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document
import javax.swing.text.PlainDocument

class LanguageItemModel(private val validator: Validator) {
    val nameDoc = PlainDocument()
    val extensionDoc = PlainDocument()
    val buildCommandDoc = PlainDocument()

    var validChangeByUserListener: ((lang: SolutionLanguage) -> Unit)? = null
    private var solutionLang: SolutionLanguage? = null
    private var notify = true

    init {
        val notifier = {
            val value = createValidValue()
            if (notify && value != null)
                validChangeByUserListener?.let { it(value) }
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
        solutionLang = item

        notify = false
        if (name != nameDoc.getText())
            nameDoc.setText(name)
        if (extension != extensionDoc.getText())
            extensionDoc.setText(extension)
        if (buildCommand != buildCommandDoc.getText())
            buildCommandDoc.setText(buildCommand)
        notify = true
    }

    private fun createValidValue(): SolutionLanguage? {
        val item = solutionLang ?: return null
        var name = nameDoc.getText()
        var extension = extensionDoc.getText()
        var buildCommand = buildCommandDoc.getText()

        val nameError = validator.validateName(name)
        val extensionError = validator.validateExtension(extension)
        val buildCommandError = validator.validateBuildCommand(buildCommand)

        // ignores dot prefix if present
        extension = if (extension.elementAtOrNull(0) == '.') extension.substring(1) else extension

        // reset invalid fields
        if (nameError != null)
            name = item.name
        if (extensionError != null)
            extension = item.extension
        if (buildCommandError != null)
            buildCommand = item.buildCommand

        return SolutionLanguage(name, extension, buildCommand, item.id)
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

    interface Validator {
        fun validateName(name: String): String?
        fun validateExtension(extension: String): String?
        fun validateBuildCommand(buildCommand: String): String?
    }
}