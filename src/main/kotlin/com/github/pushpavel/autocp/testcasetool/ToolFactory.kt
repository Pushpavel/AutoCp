package com.github.pushpavel.autocp.testcasetool

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class ToolFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
//        val component = TestcaseToolContentPanel(toolWindow)
//        val content = contentManager.factory.createContent(component, null, false)
//        contentManager.addContent(content)
    }
}
