package files.editor

import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBFont
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class ProblemPanel(model: ProblemModel) {


    private val mainPanelContent by lazy { TestcasePanel(model.testcaseModel) }

    private val mainPanel = JBPanelWithEmptyText().withEmptyText("Create New Testcase").apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(mainPanelContent)
    }

    private val sidePanel = JBList(model).also {
        it.selectionModel = model.sidePanelSelectionModel
    }.let {
        ToolbarDecorator
            .createDecorator(it)
            .createPanel()
    }
    val nameLabel = JBLabel("").apply {
        font = JBFont.h1()
        border = BorderFactory.createEmptyBorder(100, 0, 0, 0)
    }

    val groupLabel = JBLabel("").apply {
        font = JBFont.label().asItalic()
    }

    init {
        model.problemDataListener = {
            nameLabel.text = it.name
            groupLabel.text = it.group
        }
    }

    val component = panel {
        row {
            row { nameLabel() }
            row { groupLabel() }
            row {
                JBSplitter(0.3F).apply {
                    firstComponent = sidePanel
                    secondComponent = mainPanel
                }(grow, pushY)
            }
        }
    }

}