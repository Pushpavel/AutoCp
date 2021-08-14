package tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import common.helpers.UniqueNameEnforcer
import common.res.R
import common.ui.swing.editableList.EditableListView
import database.autoCp
import database.models.SolutionFile
import database.models.Testcase
import java.awt.BorderLayout
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

    private val db = project.autoCp()
    val component: JComponent

    init {
        val listComponent = EditableListView(
            testcaseListModel,
            { TestcasePanel(testcaseListModel) },
            {
                val name = testcaseNameEnforcer.buildUniqueNameWithPrefix("Testcase")
                Testcase(name, "input", "output")
            },
            "New Testcase"
        )

        component = BorderLayoutPanel().apply {
            add(panel(LCFlags.fill) {
                row {
                    label("Constraints:")
                    label("200ms").applyToComponent {
                        icon = R.icons.clock
                    }
                    label("1000KB").applyToComponent {
                        icon = R.icons.memory
                    }
                    placeholder().constraints(pushX)
                }
            }.apply {
                border = JBUI.Borders.empty(8, 8, 0, 8)
            }, BorderLayout.PAGE_START)
            add(listComponent, BorderLayout.CENTER)
        }

        Disposer.register(this, listComponent)

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



