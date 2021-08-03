package tool

import com.github.pushpavel.autocp.database.Problem
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.containers.OrderedSet
import database.autoCpDatabase
import database.models.Testcase
import tool.base.CollectionListListener
import tool.base.FileFollowedContent
import ui.poplist.PopListModel

/**
 * [FileFollowedContent] implementation that also updates viewer when file being
 * followed changes (i.e) currently selected file changes
 */
class SpecFileAdapter(
    project: Project,
    private val viewer: ProblemViewer
) : FileFollowedContent<Problem>(viewer) {

    private val db = project.autoCpDatabase()
    private var problem: Problem? = null

    override fun getDataForFile(file: VirtualFile) = db.getProblem(file.path).getOrNull()

    private val testSpecsListener = object : CollectionListListener<Testcase>() {
        override fun onChange(items: List<Testcase>) {
            // Fixme: Testcases must be implicitly used as OrderedSet
            problem?.let {
                db.updateTestcases(it, OrderedSet(items)).getOrThrow()
            }
        }
    }

    private val selectionListener = PopListModel.SelectionListener { selectedIndex ->
        problem?.let {
            db.updateProblemState(it, selectedIndex.toLong()).getOrThrow()
        }
    }

    override fun onFileFollowed(file: VirtualFile, data: Problem) {
        problem = data

        viewer.model.apply {
            listModel.addListDataListener(testSpecsListener)
            addSelectionListener(selectionListener)
        }
    }

    override fun onFileUnfollowed(file: VirtualFile, data: Problem) {
        problem = null

        viewer.model.apply {
            listModel.removeListDataListener(testSpecsListener)
            removeSelectionListener(selectionListener)
        }
    }

}