package tool

import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import database.models.Testcase
import ui.StringCellRenderer
import ui.poplist.PopList
import ui.poplist.PopListModel
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.border.EmptyBorder

class TestcasesPanel(model: PopListModel<Testcase>) : PopList<Testcase>(true, 0.25F, model) {

    override val listComponent = JBList<Testcase>().apply {
        cellRenderer = Testcase.cellRenderer()
    }

    override val listContainer = ToolbarDecorator.createDecorator(listComponent)
        .setToolbarPosition(ActionToolbarPosition.LEFT)
        .setAddAction { model.addNewOrDuplicateItem() }
        .createPanel()
        .apply {
            border = BorderFactory.createCompoundBorder(EmptyBorder(16, 16, 0, 16), border)
        }

    override val itemContainer = JBPanelWithEmptyText(BorderLayout()).withEmptyText("Create Test case")

    override val itemView = TestcaseUI(model, TestcaseModel())

}