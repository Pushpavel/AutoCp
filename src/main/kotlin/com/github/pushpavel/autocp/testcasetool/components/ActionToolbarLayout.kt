package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.ui.helpers.layoutUpdater
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import javax.swing.JComponent


class ActionToolBarLayout(
    actionGroup: ActionGroup,
    actionPlace: String = ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
    horizontal: Boolean = false
) : JBPanel<ActionToolBarLayout>(BorderLayout()) {

    private val actionToolbar = ActionManager.getInstance().createActionToolbar(actionPlace, actionGroup, horizontal)

    var content: JComponent? by layoutUpdater(null) {
        if (it != null) {
            remove(it)
            add(it, BorderLayout.CENTER)
        }
    }

    var toolBarConstraint: String by layoutUpdater(BorderLayout.LINE_START) {
        remove(actionToolbar.component)
        add(actionToolbar.component, it)
        actionToolbar.targetComponent = content
    }

    override fun invalidate() {
        super.invalidate()
    }
}