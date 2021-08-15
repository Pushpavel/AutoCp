package tool

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import common.helpers.onFileSelectionChange
import common.helpers.pathString
import common.helpers.properties
import common.helpers.toolWindowSelectedTabIndex
import database.autoCp
import tool.ui.SolutionFileSettingsPanel
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

        val tabIndexSaver = object : ContentManagerListener {
            override fun selectionChanged(event: ContentManagerEvent) {
                if (event.operation == ContentManagerEvent.ContentOperation.add)
                    project.properties.toolWindowSelectedTabIndex = event.index
            }
        }

        project.onFileSelectionChange { file ->
            contentManager.removeContentManagerListener(tabIndexSaver)
            contentManager.removeAllContents(true)

            if (file == null || !file.isValid || !db.solutionFiles.containsKey(file.pathString) || !isFileOpen(file))
                return@onFileSelectionChange

            val ui = TestcaseListPanel(project, db.solutionFiles[file.pathString]!!)
            val settingsPanel = SolutionFileSettingsPanel(project, file.pathString)

            val content = contentManager.factory.createContent(ui.component, file.presentableName, false)
            val settingsContent = contentManager.factory.createContent(settingsPanel.component, "Settings", false)

            Disposer.register(content, ui)
            Disposer.register(settingsContent, settingsPanel)

            val selectedIndex = project.properties.toolWindowSelectedTabIndex

            val contents = listOf(
                content,
                settingsContent
            )

            contentManager.addContent(contents[selectedIndex])
            contents.forEachIndexed { index, it ->
                if (index != selectedIndex)
                    contentManager.addContent(it, index)
            }

            // save selected tab index
            contentManager.addContentManagerListener(tabIndexSaver)
        }


    }


}