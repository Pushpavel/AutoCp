package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import database.models.SolutionFile
import database.models.Testcase
import ui.swing.editableList.EditableListView
import javax.swing.JComponent

class TestcaseListPanel(project: Project, solutionFile: SolutionFile) : Disposable {

    private val testcaseListModel = CollectionListModel(solutionFile.testcases)

    val component: JComponent

    init {
        component = EditableListView(testcaseListModel, {
            TestcasePanel(testcaseListModel)
        }, {
            Testcase("New Testcase", "input", "output")
        }, "New Testcase")
        Disposer.register(this, component)

    }

    override fun dispose() {}
}


