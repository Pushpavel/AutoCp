package tool

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import common.helpers.onFileSelectionChange
import database.autoCp
import tool.ui.testcaseListPanel


/**
 * [ToolWindowFactory] for creating a [ToolWindow] for editing and viewing testcases
 */
class ToolFactory : ToolWindowFactory, DumbAware {


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // use this for developing settings ui
        // ShowSettingsUtil.getInstance().showSettingsDialog(project, "Languages")

        val contentManager = toolWindow.contentManager
        val db = project.autoCp()

        project.onFileSelectionChange { file ->
            contentManager.removeAllContents(true)

            if (file == null || !file.isValid || !db.solutionFiles.containsKey(file.path) || !isFileOpen(file))
                return@onFileSelectionChange

            val ui = testcaseListPanel(db.solutionFiles[file.path]!!)

            val content = contentManager.factory.createContent(ui, file.presentableName, false)
            contentManager.addContent(content)
        }
    }
}