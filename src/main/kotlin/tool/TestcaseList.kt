package tool

import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextArea
import files.TestcaseSpec
import java.awt.Component
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer
import javax.swing.ListModel

class TestcaseList(model: ListModel<TestcaseSpec>) : JBList<TestcaseSpec>(model) {
    init {
        selectionModel = SingleSelectionModel()
        cellRenderer = Renderer()
    }
}

class Renderer : ListCellRenderer<TestcaseSpec> {
    val panel = JPanel()
    val label = JBLabel()
    val inputField = JBTextArea()
    val outputField = JBTextArea()

    init {
        panel.add(label)
        panel.add(inputField)
        panel.add(outputField)
    }

    override fun getListCellRendererComponent(
        list: JList<out TestcaseSpec>,
        extension: TestcaseSpec,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        label.text = extension.getName()
        inputField.text = extension.getName() + " Input"
        outputField.text = extension.getName() + " Output"
        return panel
    }
}