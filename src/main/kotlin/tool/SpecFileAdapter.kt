package tool

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import files.ProblemSpec
import files.ProblemSpecManager
import files.TestcaseSpec
import tool.base.CollectionListListener
import tool.base.FileFollowedContent
import ui.poplist.PopListModel
import javax.swing.event.ListSelectionListener

class SpecFileAdapter(
    project: Project,
    private val viewer: ProblemViewer
) : FileFollowedContent<ProblemSpec>(viewer) {

    private val service = project.service<ProblemSpecManager>()
    private var specDoc: Document? = null
    override fun getDataForFile(file: VirtualFile) = service.findSpec(file.path)

    private val testSpecsListener = object : CollectionListListener<TestcaseSpec>() {
        override fun onChange(items: List<TestcaseSpec>) = updateSpec { it.testcases = items.toMutableList() }
    }

    private val selectionListener = PopListModel.SelectionListener { selectedIndex ->
        updateSpec { it.selectedIndex = selectedIndex }
    }

    override fun onFileFollowed(file: VirtualFile, data: ProblemSpec) {
        viewer.model.apply {
            listModel.addListDataListener(testSpecsListener)
            addSelectionListener(selectionListener)
        }


        val specFile = service.getSpecVirtualFile(data)
            ?: throw IllegalStateException("ProblemSpec does not correspond to a spec file")

        specDoc = FileDocumentManager.getInstance().getDocument(specFile)
    }

    override fun onFileUnfollowed(file: VirtualFile, data: ProblemSpec) {
        viewer.model.apply {
            listModel.removeListDataListener(testSpecsListener)
            removeSelectionListener(selectionListener)
        }

        specDoc = null
    }


    private fun updateSpec(predicate: (ProblemSpec) -> Unit) {
        val spec = viewer.getFollowingData()
        specDoc?.let {
            if (spec == null) return
            predicate(spec)
            ApplicationManager.getApplication().runWriteAction {
                it.setText(service.specToString(spec))
            }
        }
    }
}