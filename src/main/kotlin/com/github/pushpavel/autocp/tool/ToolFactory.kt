package com.github.pushpavel.autocp.tool

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.onFileSelectionChange
import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.common.helpers.properties
import com.github.pushpavel.autocp.common.helpers.toolWindowSelectedTabIndex
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.tool.ui.AssociateFilePanel
import com.github.pushpavel.autocp.tool.ui.GeneratorPanel
import com.github.pushpavel.autocp.tool.ui.JudgeSettingsPanel
import com.github.pushpavel.autocp.tool.ui.TestcaseListPanel
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import kotlin.io.path.Path
import kotlin.io.path.name


/**
 * [ToolWindowFactory] for creating a [ToolWindow] for editing and viewing testcases
 */
class ToolFactory : ToolWindowFactory, DumbAware {


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // use this for developing settings ui
        // ShowSettingsUtil.getInstance().showSettingsDialog(project, "Languages")

        val contentManager = toolWindow.contentManager
        val solutionFiles = SolutionFiles.getInstance(project)

        val tabIndexSaver = object : ContentManagerListener {
            override fun selectionChanged(event: ContentManagerEvent) {
                if (event.operation == ContentManagerEvent.ContentOperation.add)
                    project.properties.toolWindowSelectedTabIndex = event.index
            }
        }
        val editorManager = FileEditorManager.getInstance(project)

        var callback: ((VirtualFile?) -> Unit) = {}

        callback = callback@{ file: VirtualFile? ->
            contentManager.removeContentManagerListener(tabIndexSaver)
            contentManager.removeAllContents(true)

            if (file == null || !file.isValid || !editorManager.isFileOpen(file))
                return@callback
            if (file.pathString !in solutionFiles) {
                if (file.extension !in LangSettings.instance.langs)
                    return@callback
                val ui = AssociateFilePanel(Path(file.pathString).name) {
                    solutionFiles.upsertFile(Path(file.path))
                    callback(file)
                }
                val content = contentManager.factory.createContent(ui.component, file.presentableName, false)
                contentManager.addContent(content)
                project.properties.toolWindowSelectedTabIndex = 0
                return@callback
            }

            val ui = TestcaseListPanel(project, file.pathString)
            val settingsPanel = JudgeSettingsPanel(project, file.pathString) { callback(file) }
            val generatorPanel = GeneratorPanel(project, file.pathString)

            val content = contentManager.factory.createContent(ui.component, file.presentableName, false)
            val settingsContent = contentManager.factory.createContent(settingsPanel.component, "Judge", false)
            val generatorContent = contentManager.factory.createContent(generatorPanel.component, "Generator", false)

            Disposer.register(content, ui)
            Disposer.register(settingsContent, settingsPanel)
            Disposer.register(generatorContent, generatorPanel)

            val selectedIndex = project.properties.toolWindowSelectedTabIndex

            val contents = listOf(
                content,
                settingsContent,
                generatorContent,
            )

            contentManager.addContent(contents[selectedIndex])
            contents.forEachIndexed { index, it ->
                if (index != selectedIndex)
                    contentManager.addContent(it, index)
            }

            // save selected tab index
            contentManager.addContentManagerListener(tabIndexSaver)
        }


        project.onFileSelectionChange { callback(it) }

    }


}