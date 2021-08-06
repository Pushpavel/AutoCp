package common.helpers

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

fun Project.onFileSelectionChange(action: FileEditorManager.(VirtualFile?) -> Unit) {
    val editorManager = FileEditorManager.getInstance(this)
    editorManager.action(editorManager.selectedFiles.firstOrNull())

    messageBus.connect().subscribe(
        FileEditorManagerListener.FILE_EDITOR_MANAGER,
        object : FileEditorManagerListener {
            override fun selectionChanged(event: FileEditorManagerEvent) {
                editorManager.action(editorManager.selectedFiles.firstOrNull())
            }
        }
    )
}