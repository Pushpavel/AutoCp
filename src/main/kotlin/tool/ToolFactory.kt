package tool

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import common.helpers.onFileSelectionChange
import common.helpers.pathString
import database.autoCp
import tool.ui.TestcaseListPanel


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

            if (file == null || !file.isValid || !db.solutionFiles.containsKey(file.path.pathString) || !isFileOpen(file))
                return@onFileSelectionChange

            val ui = TestcaseListPanel(project, db.solutionFiles[file.path.pathString]!!)

            val content = contentManager.factory.createContent(ui.component, file.presentableName, false)
            Disposer.register(content, ui)
            contentManager.addContent(content)
        }
    }
}