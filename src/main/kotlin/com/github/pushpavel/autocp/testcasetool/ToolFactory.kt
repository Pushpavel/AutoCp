package com.github.pushpavel.autocp.testcasetool

import com.github.pushpavel.autocp.testcasetool.components.TestcaseToolBars
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.AncestorListenerAdapter
import com.intellij.ui.layout.panel
import javax.swing.event.AncestorEvent

class ToolFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val component = toolWindowContentUI(toolWindow)
        val content = contentManager.factory.createContent(component, null, false)
        contentManager.addContent(content)
    }

    private fun toolWindowContentUI(toolWindow: ToolWindow): TestcaseToolBars {
        val root = TestcaseToolBars(
            toolWindow.anchor != ToolWindowAnchor.BOTTOM,
            panel { row { label("content") } }
        )

        // listen for ancestor changes for reacting to toolWindow anchor changes
        root.addAncestorListener(object : AncestorListenerAdapter() {
            override fun ancestorAdded(event: AncestorEvent?) {
                root.rightAnchored = toolWindow.anchor != ToolWindowAnchor.BOTTOM
                root.applyUpdates()
                root.invalidate()
            }
        })

        return root
    }
}
