package tool

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import database.AutoCpDB
import database.models.ProblemData
import database.models.ProblemState
import database.models.TestcaseSpec
import tool.base.CollectionListListener
import tool.base.FileFollowedContent
import ui.poplist.PopListModel

class SpecFileAdapter(
    project: Project,
    private val viewer: ProblemViewer
) : FileFollowedContent<ProblemData>(viewer) {

    private val db = project.service<AutoCpDB>()
    private var problemId: String? = null

    override fun getDataForFile(file: VirtualFile) = db.getProblemData(file.path)

    private val testSpecsListener = object : CollectionListListener<TestcaseSpec>() {
        override fun onChange(items: List<TestcaseSpec>) = db.updateTestcaseSpecs(items)
    }

    private val selectionListener = PopListModel.SelectionListener { selectedIndex ->
        problemId?.let {
            db.updateProblemState(ProblemState(it, selectedIndex))
        }
    }

    override fun onFileFollowed(file: VirtualFile, data: ProblemData) {
        problemId = data.state.problemId

        viewer.model.apply {
            listModel.addListDataListener(testSpecsListener)
            addSelectionListener(selectionListener)
        }
    }

    override fun onFileUnfollowed(file: VirtualFile, data: ProblemData) {
        problemId = null

        viewer.model.apply {
            listModel.removeListDataListener(testSpecsListener)
            removeSelectionListener(selectionListener)
        }
    }

}