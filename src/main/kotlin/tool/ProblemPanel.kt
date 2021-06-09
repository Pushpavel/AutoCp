package tool

import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import files.TestcaseSpec
import ui.poplist.PopList
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListModel
import javax.swing.border.EmptyBorder

class ProblemPanel : PopList<TestcaseSpec>(
    true, 0.25F, CollectionListModel(
        TestcaseSpec(0, "1 0 11", "11 1 1 1 11 "),
        TestcaseSpec(1, "1 0 11", "11 1 1 1 11 "),
        TestcaseSpec(2, "1 0 11", "11 1 1 1 11 "),
        TestcaseSpec(3, "1 0 11", "11 1 1 1 11 "),
    )
) {

    override val listComponent = JBList<TestcaseSpec>()

    override val listContainer = ToolbarDecorator.createDecorator(listComponent)
        .setToolbarPosition(ActionToolbarPosition.LEFT)
        .createPanel()
        .apply {
            border = BorderFactory.createCompoundBorder(EmptyBorder(16, 16, 0, 16), border)
        }

    override val itemContainer = JBPanelWithEmptyText(BorderLayout()).withEmptyText("Create Test case")

    override val itemView = object : ItemView<TestcaseSpec> {
        override val component = JBLabel("Item view")

        override fun updateView(item: TestcaseSpec) {
            component.text = item.getName()
        }

    }

}