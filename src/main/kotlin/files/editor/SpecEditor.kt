package files.editor

import com.google.gson.Gson
import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.BulkAwareDocumentListener
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import common.Constants
import files.ProblemSpec

class SpecEditor(private val specFile: VirtualFile) : FileEditorBase() {

    private val gson = Gson()

    private val specDocument = FileDocumentManager.getInstance().getDocument(specFile)!!

    private val model = ProblemModel()
    private val mainPanel = ProblemPanel(model)

    init {
        reset()
        specDocument.addDocumentListener(object : BulkAwareDocumentListener.Simple {
            override fun documentChanged(event: DocumentEvent) {
                if (isModified)
                    reset()
            }
        })

    }

    override fun isModified(): Boolean {
        val spec = gson.fromJson(specDocument.text, ProblemSpec::class.java)
        return spec.selectedIndex != model.sidePanelSelectionModel.minSelectionIndex.takeIf { it != -1 }
                || spec.testcases.zip(model.items).any { it.first != it.second }
    }

    fun apply() {
        val spec = gson.fromJson(specDocument.text, ProblemSpec::class.java)
        spec.selectedIndex = model.sidePanelSelectionModel.minSelectionIndex.takeIf { it != -1 }
        spec.testcases = model.items
        ApplicationManager.getApplication().invokeLater {
            specDocument.setText(gson.toJson(spec))
        }
    }

    fun reset() {
        val spec = gson.fromJson(specDocument.text, ProblemSpec::class.java)
        model.updateState(ProblemModel.ProblemData(spec.name, spec.group))
        model.removeAll()
        model.addAll(0, spec.testcases)

        spec.selectedIndex?.let {
            if (model.sidePanelSelectionModel.minSelectionIndex != it)
                model.sidePanelSelectionModel.setSelectionInterval(it, it)

        }
    }

    override fun getComponent() = mainPanel.component

    override fun getName() = Constants.SPEC_EDITOR_NAME

    override fun getPreferredFocusedComponent() = mainPanel.component

    override fun getFile() = specFile
}