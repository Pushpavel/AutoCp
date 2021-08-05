package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import database.autoCp
import database.models.SolutionFile
import ui.swing.editableList.EditableListView
import javax.swing.JComponent

class TestcaseListPanel(project: Project, solutionFile: SolutionFile) : Disposable {

    private val testcaseListModel = CollectionListModel(solutionFile.testcases)

    private val db = project.autoCp()

    val component: JComponent

    init {

        component = EditableListView(testcaseListModel) {
            TestcasePanel()
        }.also {
            Disposer.register(this, it)
        }
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}


