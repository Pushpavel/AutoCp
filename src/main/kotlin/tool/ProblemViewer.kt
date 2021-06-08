package tool

import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import files.editor.IOField
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.border.EmptyBorder

class ProblemViewer : JBSplitter(true, 0.25F) {

    init {

        val list = JBList("Test case #1", "Test case #2", "Test case #3", "Test case #4")
        firstComponent =
            ToolbarDecorator.createDecorator(list).setToolbarPosition(ActionToolbarPosition.LEFT).setAddAction {
            }.apply {
                disableUpDownActions()
            }.createPanel().apply {
                border = BorderFactory.createCompoundBorder(EmptyBorder(16, 16, 0, 16), border)
            }


        secondComponent = JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(0, 16, 0, 16)

            add(IOField("Input:", null))
            add(IOField("Output:", null))
        }


    }
}
