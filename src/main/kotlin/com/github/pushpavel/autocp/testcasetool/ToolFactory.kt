package com.github.pushpavel.autocp.testcasetool

import com.github.pushpavel.autocp.testcasetool.actions.actionGroup
import com.github.pushpavel.autocp.testcasetool.components.ActionToolBarLayout
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.layout.panel
import java.awt.BorderLayout

class ToolFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val component = toolWindowContentUI()

        val content = contentManager.factory.createContent(component, null, false)
        contentManager.addContent(content)
    }

    private fun toolWindowContentUI() = ActionToolBarLayout(actionGroup()).apply {
        content = panel {
            row {
                label("super")
            }
            row {
                button("Super") {
                    toolBarConstraint = if (toolBarConstraint == BorderLayout.LINE_START) {
                        BorderLayout.NORTH
                    } else {
                        BorderLayout.LINE_START
                    }
                }
            }
        }
    }
}
