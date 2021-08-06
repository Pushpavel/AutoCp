package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import common.helpers.UniqueNameEnforcer
import database.autoCp
import database.models.SolutionFile
import database.models.Testcase
import ui.swing.editableList.EditableListView
import javax.swing.JComponent
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class TestcaseListPanel(project: Project, private val solutionFile: SolutionFile) : Disposable, ListDataListener {

    private val testcaseListModel = CollectionListModel(solutionFile.testcases)
    private val testcaseNameEnforcer = UniqueNameEnforcer(
        Regex("^(.*) #([0-9]+)\$"),
        { p, s -> "$p #$s" },
        { testcaseListModel.items.map { it.name } }
    )

    val db = project.autoCp()
    val component: JComponent

    init {
        component = EditableListView(
            testcaseListModel,
            { TestcasePanel(testcaseListModel) },
            {
                val name = testcaseNameEnforcer.buildUniqueNameWithPrefix("Testcase")
                Testcase(name, "input", "output")
            },
            "New Testcase"
        )
        Disposer.register(this, component)

        testcaseListModel.addListDataListener(this)
    }


    override fun intervalAdded(event: ListDataEvent) {
        updateTestcases { testcases ->
            for (i in event.index0..event.index1)
                testcases.add(i, testcaseListModel.items[i])
        }
    }

    override fun intervalRemoved(event: ListDataEvent) {
        updateTestcases { testcases ->
            testcases.subList(event.index0, event.index1 + 1).clear()
        }
    }

    override fun contentsChanged(event: ListDataEvent) {
        updateTestcases { testcases ->
            for (i in event.index0..event.index1)
                testcases[i] = testcaseListModel.items[i]
        }
    }


    override fun dispose() {
        testcaseListModel.removeListDataListener(this)
    }


    private fun updateTestcases(update: (MutableList<Testcase>) -> Unit) {
        val testcases = db.solutionFiles[solutionFile.pathString]?.testcases?.toMutableList() ?: return
        update(testcases)
        db.updateSolutionFile(solutionFile.copy(testcases = testcases))
    }
}



