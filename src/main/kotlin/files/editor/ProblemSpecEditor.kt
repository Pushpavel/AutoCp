package files.editor

import com.google.gson.Gson
import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import common.Constants
import files.ProblemSpec
import files.editor.ui.ProblemSpecPanel

class ProblemSpecEditor(private val solutionFile: VirtualFile, specFile: VirtualFile) : FileEditorBase(),
    DocumentListener {

    private val gson = Gson()
    private var specDoc = FileDocumentManager.getInstance().getDocument(specFile)
    private val specPanel: ProblemSpecPanel

    init {
        val spec = getSpec()
        specDoc?.addDocumentListener(this)
        specPanel = ProblemSpecPanel(spec, ::setSpec)
    }

    override fun documentChanged(event: DocumentEvent) {
        val spec = getSpec()
        specPanel.receiveState(spec)
    }

    private fun getSpec() = specDoc?.let { gson.fromJson(it.text, ProblemSpec::class.java) }

    private fun setSpec(spec: ProblemSpec?) {
        ApplicationManager.getApplication().runWriteAction {
            specDoc?.setText(gson.toJson(spec))
        }
    }

    override fun getName() = Constants.SPEC_EDITOR_NAME

    override fun getComponent() = specPanel.component

    override fun getPreferredFocusedComponent() = specPanel.component

    override fun getFile() = solutionFile

    override fun dispose() {
        specDoc?.removeDocumentListener(this)
        super.dispose()
    }
}