package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.testcasetool.actions.actionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.SwingConstants

abstract class TestcaseToolBarsPanel : JBPanel<TestcaseToolBarsPanel>(BorderLayout()) {

    var bottomAnchored: Boolean = true
    abstract val content: JComponent

    private val runActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        actionGroup(),
        !bottomAnchored
    )

    private val testcaseActionBar = ActionManager.getInstance().createActionToolbar(
        ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
        testcaseActionGroup(),
        bottomAnchored
    )

    private val innerPanel = BorderLayoutPanel()

    open fun applyUpdates() {
        runActionBar.targetComponent = content
        testcaseActionBar.targetComponent = content

        runActionBar.setOrientation(if (!bottomAnchored) SwingConstants.HORIZONTAL else SwingConstants.VERTICAL)
        remove(runActionBar.component)
        add(runActionBar.component, if (!bottomAnchored) BorderLayout.PAGE_START else BorderLayout.LINE_START)

        add(innerPanel.apply {
            testcaseActionBar.setOrientation(if (!bottomAnchored) SwingConstants.VERTICAL else SwingConstants.HORIZONTAL)
            remove(testcaseActionBar.component)
            add(testcaseActionBar.component, if (!bottomAnchored) BorderLayout.LINE_START else BorderLayout.PAGE_START)

            remove(content)
            add(content, BorderLayout.CENTER)
        }, BorderLayout.CENTER)

    }
}