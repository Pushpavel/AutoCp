package tool

import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.border.EmptyBorder

class ProblemViewer : JBPanel<ProblemViewer>(BorderLayout()) {

    init {
        add(ProblemToolbar().component, BorderLayout.WEST)

        add(panel {
            hideableRow("Testcase #1") {
                textArea({ "asd" }, {})
            }
            hideableRow("Testcase #2") {
                textArea({ "asd" }, {})
            }
            hideableRow("Testcase #3") {
                textArea({ "asd" }, {})
            }
            hideableRow("Testcase #4") {
                textArea({ "asd" }, {})
            }
        }.apply {
            val innerPadding = EmptyBorder(10, 0, 10, 0)
            val leftLinedBorder = JBUI.Borders.customLineLeft(JBUI.CurrentTheme.ToolWindow.borderColor())
            border = BorderFactory.createCompoundBorder(leftLinedBorder, innerPadding)
        }, BorderLayout.CENTER)
    }
}
