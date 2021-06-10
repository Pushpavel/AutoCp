package tool

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

class SpecFileAdapter(
    project: Project,
    private val viewer: ProblemViewer
) : FileFollowedContent<ProblemSpec>(viewer) {

    private val service = project.service<ProblemSpecManager>()
    private var specDoc: Document? = null
    override fun getDataForFile(file: VirtualFile) = service.findSpec(file.path)


    override fun onFileFollowed(file: VirtualFile, data: ProblemSpec) {
        print("+ followed ${file.name}\n")

        viewer.model.addListDataListener(testSpecsListener)
        val specFile = service.getSpecVirtualFile(data)
            ?: throw IllegalStateException("ProblemSpec does not correspond to a spec file")

        specDoc = FileDocumentManager.getInstance().getDocument(specFile)
    }

    override fun onFileUnfollowed(file: VirtualFile, data: ProblemSpec) {
        viewer.model.removeListDataListener(testSpecsListener)

        specDoc = null
    }

    private val testSpecsListener = object : CollectionListListener<TestcaseSpec>() {

        override fun onChange(items: List<TestcaseSpec>) {
            val spec = viewer.getFollowingData()
            specDoc?.let {
                if (spec == null) return
                spec.testcases = items.toMutableList()
                it.setText(service.specToString(spec))
            }
        }
    }
}