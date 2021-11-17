package com.github.pushpavel.autocp.common.helpers

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.pushpavel.autocp.common.res.AutoCpFiles
import java.nio.file.*
import java.util.stream.Collectors
import kotlin.io.path.Path
import kotlin.io.path.pathString

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


    return Files.list(dirPath).collect(Collectors.toList())
}

fun FileTemplate.constructFileNameWithExt(name: String): String {
    return name + if (extension.isNotBlank()) ".$extension" else ""
}

val String.pathString: String
    get() = try {
        Path(this).pathString
    } catch (e: Exception) {
        this
    }

val VirtualFile.pathString get() = path.pathString