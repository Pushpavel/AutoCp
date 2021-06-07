package files.editor

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.EditorTextField
import java.awt.Dimension
import kotlin.math.min

class IOField(labelText: String, document: Document) : LabeledComponent<IOField.Field>() {

    init {
        text = labelText
        component = Field(document)
    }

    override fun getPreferredSize(): Dimension {
        val preferredSize = super.getPreferredSize()
        preferredSize.height = min(preferredSize.height, parent.height / 2)
        return preferredSize
    }

    class Field(document: Document) : EditorTextField(document, null, FileTypes.PLAIN_TEXT) {
        override fun createEditor(): EditorEx {
            val editor = super.createEditor()
            editor.isOneLineMode = false
            editor.setHorizontalScrollbarVisible(true)
            editor.setVerticalScrollbarVisible(true)
            updateBorder(editor)
            return editor
        }
    }
}