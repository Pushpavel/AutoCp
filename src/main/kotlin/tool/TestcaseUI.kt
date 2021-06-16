package tool

import com.intellij.ui.components.JBPanel
import database.models.Testcase
import ui.IOField
import ui.poplist.PopList
import ui.poplist.PopListModel
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class TestcaseUI(popModel: PopListModel<Testcase>, private val model: TestcaseModel) : PopList.ItemView<Testcase> {

    override val component = JBPanel<JBPanel<*>>().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
        add(IOField("Input:", model.inputDoc).apply {
            border = BorderFactory.createEmptyBorder(0, 0, 8, 0)
        })
        add(IOField("Output:", model.outputDoc))
    }

    override fun updateView(item: Testcase) = model.update(item)

    init {
        model.changeByUserListener = { testcase ->
            val items = popModel.listModel.items
            val changedIndex = items.indexOfFirst { it.name == testcase.name }

            if (changedIndex != -1 && !items[changedIndex].equals(testcase))
                popModel.listModel.setElementAt(testcase, changedIndex)
        }
    }
}