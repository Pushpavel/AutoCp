package com.github.pushpavel.autocp.testcasetool.datalayer

import com.github.pushpavel.autocp.build.settings.LangSettings
import com.github.pushpavel.autocp.common.helpers.*
import com.github.pushpavel.autocp.core.persistance.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.solutions.Solutions
import com.github.pushpavel.autocp.testcasetool.components.TestcaseToolContentPanel
import com.github.pushpavel.autocp.tool.ui.AssociateFilePanel
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.swing.JComponent
import kotlin.io.path.Path
import kotlin.io.path.name

class TestcaseSolutionContentManager(private val project: Project, private val toolWindow: ToolWindow) {
    private val editorManager = FileEditorManager.getInstance(project)
    private val contentManager = toolWindow.contentManager

    private val solutions = project.service<Solutions>()

    private var currentFile: VirtualFile? = null
    private val fileScoped by DisposableScope { currentFile = null }
    private val contentScoped by DisposableScope()

    /**
     * Rebuilds the toolWindow content with currently selected [file]
     */
    fun changeSolutionFile(file: VirtualFile?): Boolean {
        fileScoped.doDisposal() // clear previous file related resources
        currentFile = file

        // show no content if no valid file
        if (file == null || !file.isValid || !editorManager.isFileOpen(file))
            return false

        val solutionKey = file.pathString
        val component = createComponent(solutionKey, file.extension)

        if (component != null) {
            val content = contentManager.factory.createContent(component, null /*TODO*/, false)
            contentManager.addContent(content)
            // remove content scope on file change
            Disposer.register(fileScoped, contentScoped)
            Disposer.register(contentScoped) { contentManager.removeContent(content, true) }
            return true
        }
        return false
    }

    /**
     * Creates a component for the given [solutionKey] and [extension]
     * Provides UI to add [solutionKey] if not already added to [solutions]
     */
    private fun createComponent(solutionKey: String, extension: String?): JComponent? {
        if (solutionKey !in solutions) {
            if (extension !in LangSettings.instance.langs)
                return null
            // ui to associate currently selected file as Solution
            return AssociateFilePanel(Path(solutionKey).name) {
                solutions.put(Solution(solutionKey))
                // refresh the toolWindow content
                changeSolutionFile(currentFile)
            }.component
        }

        val scope = defaultScope().cancelBy(fileScoped)

        val contentPanel = TestcaseToolContentPanel(project, toolWindow, solutions[solutionKey]!!)

        // listen for changes in the solution
        scope.launch {
            solutions.onKey(solutionKey).collect {
                if (it != null)
                    contentPanel.solution = it
                else
                    contentScoped.doDisposal()
            }
        }
        return contentPanel.testcaseListPanel
    }
}