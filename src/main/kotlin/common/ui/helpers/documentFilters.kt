package common.ui.helpers

import com.intellij.ui.components.JBTextField
import javax.swing.text.AbstractDocument
import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter

fun JBTextField.allowOnlyPositiveIntegers() {
    (document as AbstractDocument).documentFilter = object : DocumentFilter() {
        override fun insertString(fb: FilterBypass?, offset: Int, string: String, attr: AttributeSet?) {
            super.insertString(fb, offset, string.replace("\\D+".toRegex(), ""), attr)
        }

        override fun replace(fb: FilterBypass?, offset: Int, length: Int, text: String, attrs: AttributeSet?) {
            super.replace(fb, offset, length, text.replace("\\D+".toRegex(), ""), attrs)
        }
    }
}