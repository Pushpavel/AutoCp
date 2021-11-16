package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.SwingConstants

class ActionContainer(
    primaryActions: ActionGroup,
    secondaryActions: ActionGroup,
    val content: JComponent
) : BorderLayoutPanel() {

    private val innerPanel = BorderLayoutPanel().apply { add(content, BorderLayout.CENTER) }

    private val primaryActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        primaryActions,
        true
    ).apply { targetComponent = content }

    private val secondaryActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        secondaryActions,
        false
    ).apply { targetComponent = content }

    var bottomAnchored: Boolean by setter(false) {
        primaryActionBar.setOrientation(if (!it) SwingConstants.HORIZONTAL else SwingConstants.VERTICAL)
        remove(primaryActionBar.component)
        add(primaryActionBar.component, if (!it) BorderLayout.PAGE_START else BorderLayout.LINE_START)

        secondaryActionBar.setOrientation(if (!it) SwingConstants.VERTICAL else SwingConstants.HORIZONTAL)
        innerPanel.remove(secondaryActionBar.component)

        // TODO: hide secondary action bar if no actions
        innerPanel.add(secondaryActionBar.component, if (!it) BorderLayout.LINE_START else BorderLayout.PAGE_START)

        add(innerPanel, BorderLayout.CENTER)
    }
}