package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.testcasetool.actions.actionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.SwingConstants

class TestcaseToolBars(var rightAnchored: Boolean = true, val content: JComponent) :
    JBPanel<TestcaseToolBars>(BorderLayout()) {

    private val runActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        actionGroup(),
        rightAnchored
    )

    private val testcaseActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        testcaseActionGroup(),
        !rightAnchored
    )

    private val innerPanel = BorderLayoutPanel()

    fun applyUpdates() {
        runActionBar.targetComponent = content
        testcaseActionBar.targetComponent = content

        runActionBar.setOrientation(if (rightAnchored) SwingConstants.HORIZONTAL else SwingConstants.VERTICAL)
        remove(runActionBar.component)
        add(runActionBar.component, if (rightAnchored) BorderLayout.PAGE_START else BorderLayout.LINE_START)

        add(innerPanel.apply {
            testcaseActionBar.setOrientation(if (rightAnchored) SwingConstants.VERTICAL else SwingConstants.HORIZONTAL)
            remove(testcaseActionBar.component)
            add(testcaseActionBar.component, if (rightAnchored) BorderLayout.LINE_START else BorderLayout.PAGE_START)

            remove(content)
            add(content, BorderLayout.CENTER)
        }, BorderLayout.CENTER)

    }
}