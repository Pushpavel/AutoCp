package tool

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content


/**
 * [ToolWindowFactory] for creating a [ToolWindow] for editing and viewing testcases
 */
class ToolFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val viewer = ProblemViewer()
        var oldContent: Content? = null

        // replacing content of toolWindow on changes
        viewer.setContentListener { newContent ->
            oldContent?.let { content -> toolWindow.contentManager.removeContent(content, false) }
            newContent?.let { content -> toolWindow.contentManager.addContent(content) }
            oldContent = newContent
        }

        val specFileAdapter = SpecFileAdapter(project, viewer)

        // initial selected files
        val editorManager = FileEditorManager.getInstance(project)
        specFileAdapter.followFiles(editorManager.selectedFiles.toList())

        // further selected files
        project.messageBus.connect().subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {
                override fun selectionChanged(event: FileEditorManagerEvent) {
                    specFileAdapter.followFiles(editorManager.selectedFiles.toList())
                }
            }
        )
    }
}