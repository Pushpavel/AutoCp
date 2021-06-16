package tool

import com.intellij.openapi.editor.EditorFactory
import com.intellij.ui.components.JBPanel
import database.models.Testcase
import ui.IOField
import ui.poplist.PopList
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class TestcaseUI(model: TestcaseModel) : PopList.ItemView<Testcase> {
    val doc = EditorFactory.getInstance().createDocument("")
    val doc2 = EditorFactory.getInstance().createDocument("")
    override val component = JBPanel<JBPanel<*>>().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
        add(IOField("Input:", doc).apply {
            border = BorderFactory.createEmptyBorder(0, 0, 8, 0)
        })
        add(IOField("Output:", doc2))

    }

    override fun updateView(item: Testcase) {

    }
}