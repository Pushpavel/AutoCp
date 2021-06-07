package files.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.util.Ref
import com.intellij.ui.components.JBPanel
import files.TestcaseSpec
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class TestcasePanel(model: Model) : JBPanel<TestcasePanel>() {
    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = BorderFactory.createEmptyBorder(0, 8, 0, 16)

        add(IOField("Input:", model.inputDoc))
        add(IOField("Output:", model.outputDoc))
    }

    class Model {
        internal val inputDoc = EditorFactory.getInstance().createDocument("")
        internal val outputDoc = EditorFactory.getInstance().createDocument("")
        private val itemRef = Ref<TestcaseSpec?>()

        fun applyItem(item: TestcaseSpec?) {
            ApplicationManager.getApplication().runWriteAction {
                inputDoc.setText(item?.input ?: "")
                outputDoc.setText(item?.output ?: "")
            }

            setCorrespondingItem(item)
        }

        fun createItemValue(): TestcaseSpec? {
            getCorrespondingItem() ?: return null
            return TestcaseSpec(0, inputDoc.text, outputDoc.text)
        }

        fun setCorrespondingItem(item: TestcaseSpec?) = this.itemRef.set(item)

        fun getCorrespondingItem(): TestcaseSpec? = this.itemRef.get()
    }
}