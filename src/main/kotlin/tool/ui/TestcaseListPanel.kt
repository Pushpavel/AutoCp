package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import common.helpers.UniqueNameEnforcer
import database.models.SolutionFile
import database.models.Testcase
import ui.swing.editableList.EditableListView
import javax.swing.JComponent

class TestcaseListPanel(project: Project, solutionFile: SolutionFile) : Disposable {

    private val testcaseListModel = CollectionListModel(solutionFile.testcases)
    private val testcaseNameEnforcer = UniqueNameEnforcer(
        Regex("^(.*) #([0-9]+)\$"),
        { p, s -> "$p #$s" },
        { testcaseListModel.items.map { it.name } }
    )

    val component: JComponent

    init {
        component = EditableListView(testcaseListModel, {
            TestcasePanel(testcaseListModel)
        }, {
            Testcase(testcaseNameEnforcer.buildUniqueNameWithPrefix("Testcase"), "input", "output")
        }, "New Testcase")
        Disposer.register(this, component)

    }

    override fun dispose() {}
}


