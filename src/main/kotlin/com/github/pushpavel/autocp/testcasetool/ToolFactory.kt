package com.github.pushpavel.autocp.testcasetool

import com.github.pushpavel.autocp.common.helpers.onFileSelectionChange
import com.github.pushpavel.autocp.testcasetool.datalayer.TestcaseSolutionContentManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class ToolFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val manager = TestcaseSolutionContentManager(project, toolWindow)
        project.onFileSelectionChange { manager.changeSolutionFile(it) }
    }
}
