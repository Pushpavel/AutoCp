package common.helpers

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import res.AutoCpFiles
import java.nio.file.*
import kotlin.streams.toList

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

fun listPathsInDirectoryInResources(relativePath: String): List<Path> {
    // https://stackoverflow.com/questions/50469600/how-do-you-list-all-files-in-the-resources-folder-java-scala
    val uri = AutoCpFiles::class.java.getResource(relativePath)?.toURI()
        ?: throw Exception("$relativePath does not exists")

    val dirPath = try {
        Paths.get(uri)
    } catch (e: FileSystemNotFoundException) {
        // If this is thrown, then it means that we are running the JAR directly (example: not from an IDE)
        val env = mutableMapOf<String, String>()
        FileSystems.newFileSystem(uri, env).getPath(relativePath)
    }


    return Files.list(dirPath).toList()
}