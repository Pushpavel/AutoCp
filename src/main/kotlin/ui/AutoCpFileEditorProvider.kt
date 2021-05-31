package ui

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import services.AutoCpFilesService

class AutoCpFileEditorProvider : FileEditorProvider {
    companion object {
        private const val EDITOR_TYPE_ID = "autocp-problem-editor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (file.extension == "autocp")
            return false
        return project.service<AutoCpFilesService>().getAutoCpFile(file.nameWithoutExtension) != null //    TODO : Check file extension
    }

    override fun createEditor(project: Project, file: VirtualFile) = AutoCpFileEditor()

    override fun getEditorTypeId() = EDITOR_TYPE_ID

    override fun getPolicy() = FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR


}