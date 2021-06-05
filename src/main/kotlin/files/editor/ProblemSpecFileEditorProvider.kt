package files.editor

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rd.util.printlnError
import common.Constants
import files.ProblemSpec
import files.ProblemSpecFinder

class ProblemSpecFileEditorProvider : FileEditorProvider {
    companion object {
        private const val EDITOR_TYPE_ID = "autocp-problem-spec-editor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (file.extension !in Constants.SupportedSolutionFileExtensions)
            return false
        val specFinder = project.service<ProblemSpecFinder>()

        return try {
            specFinder.find(file.path) != null
        } catch (e: Exception) {
            false
        }
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val specFile = project.service<ProblemSpecFinder>().find(file.path)?.file
        val specVirtualFile = specFile?.let { LocalFileSystem.getInstance().findFileByIoFile(it) }

        return ProblemSpecEditor(file, specVirtualFile!!)
    }

    override fun getEditorTypeId() = EDITOR_TYPE_ID

    override fun getPolicy() = FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR
}