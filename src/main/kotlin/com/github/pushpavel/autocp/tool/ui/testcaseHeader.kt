package com.github.pushpavel.autocp.tool.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBFont
import com.github.pushpavel.autocp.database.models.Testcase
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel

fun testcaseHeader(
    model: CollectionListModel<Testcase>,
    titleLabel: JBLabel,
    testcaseIndex: () -> Int?,
): JBPanel<JBPanel<*>> {
    val jbPanel = JBPanel<JBPanel<*>>(BorderLayout())

    titleLabel.apply {
        font = JBFont.label()
        border = BorderFactory.createEmptyBorder(4, 0, 4, 0)
    }

    val panel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
    }

    panel.add(titleLabel)
    jbPanel.add(panel, BorderLayout.CENTER)


    val actionGroup = DefaultActionGroup(
        listOf(TestcaseDeleteAction(model, testcaseIndex))
    )

    jbPanel.add(
        ActionManager
            .getInstance()
            .createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
                layoutPolicy = ActionToolbar.NOWRAP_LAYOUT_POLICY
            }
            .component,
        BorderLayout.LINE_END
    )

    return jbPanel
}


class TestcaseDeleteAction(val model: CollectionListModel<Testcase>, val testcaseIndex: () -> Int?) : DumbAwareAction(
    "Delete Testcase",
    "Deletes the Testcase in Testcase viewer",
    AllIcons.General.Remove
) {

    override fun actionPerformed(e: AnActionEvent) {
        testcaseIndex()?.let {
            model.remove(it)
        }
    }

}