package settings

import plugin.settings.SolutionLanguage
import javax.swing.text.PlainDocument

class LanguageItemModel {
    val nameDoc = PlainDocument()
    val extensionDoc = PlainDocument()
    val buildCommandDoc = PlainDocument()

    fun update(item: SolutionLanguage) {
        nameDoc.setText(item.name)
        extensionDoc.setText(item.extension)
        buildCommandDoc.setText(item.buildCommand)
    }

    fun createValue(): SolutionLanguage {
        val name = nameDoc.getText(0, nameDoc.length)
        val extension = extensionDoc.getText(0, extensionDoc.length)
        val buildCommand = buildCommandDoc.getText(0, buildCommandDoc.length)

        return SolutionLanguage(name, extension, buildCommand)
    }

    private fun PlainDocument.setText(text: String) {
        this.replace(0, this.length, text, null)
    }
}