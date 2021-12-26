package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.SwingConstants

class ActionContainer(
    primaryActions: ActionGroup,
    val content: JComponent
) : BorderLayoutPanel() {

    private val innerPanel = BorderLayoutPanel().apply {
        add(content, BorderLayout.CENTER)
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
    }

    private val primaryActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        primaryActions,
        true
    ).apply {
        setTargetComponent(content)
    }

    var bottomAnchored: Boolean by setter(false) {
        primaryActionBar.setOrientation(if (!it) SwingConstants.HORIZONTAL else SwingConstants.VERTICAL)
        primaryActionBar.component.border = JBUI.Borders.customLine(
            JBColor.border(),
            0, 0, if (!it) 1 else 0, if (it) 1 else 0
        )

        remove(primaryActionBar.component)
        add(primaryActionBar.component, if (!it) BorderLayout.PAGE_START else BorderLayout.LINE_START)

        add(innerPanel, BorderLayout.CENTER)
    }
}