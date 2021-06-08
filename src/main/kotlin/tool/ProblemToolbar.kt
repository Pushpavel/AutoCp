package tool

import com.intellij.openapi.actionSystem.*
import com.intellij.ui.CommonActionsPanel
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.JBUI
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.border.EmptyBorder

class ProblemToolbar {

    val group = DefaultActionGroup().apply {
        add(object : AnAction() {
            override fun actionPerformed(e: AnActionEvent) {
                print("Performed")
            }
        })
    }


    val component =
        ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, false).component
}