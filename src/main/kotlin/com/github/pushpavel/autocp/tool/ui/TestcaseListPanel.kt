package com.github.pushpavel.autocp.tool.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.layout.ComponentPredicate
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import com.github.pushpavel.autocp.common.helpers.UniqueNameEnforcer
import com.github.pushpavel.autocp.common.helpers.isItemsEqual
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.swing.editableList.EditableListView
import com.github.pushpavel.autocp.database.autoCp
import com.github.pushpavel.autocp.database.models.Testcase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class TestcaseListPanel(project: Project, private val pathString: String) : Disposable, ListDataListener {

    private val scope = mainScope()
    private val db = project.autoCp()
    private val flow = db.solutionFilesFlow.map { it[pathString] }.filterNotNull()
    private var resetting = false

    private val testcaseListModel = CollectionListModel<Testcase>()
    private val testcaseNameEnforcer = UniqueNameEnforcer(
        Regex("^(.*) #([0-9]+)\$"),
        { p, s -> "$p #$s" },
        { testcaseListModel.items.map { it.name } }
    )

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
                    label("").applyToComponent {
                        icon = R.icons.clock
                        scope.launch {
                            flow.collect {
                                text = "${it.timeLimit}ms"
                            }
                        }
                    }
                    placeholder().constraints(pushX)

                    button("Reset Sample Testcases") {
                        val file = db.solutionFiles[pathString]
                        val problem = file?.getLinkedProblem(db)
                        if (file == null || problem == null) return@button
                        val testcases = file.testcases.filter { !it.name.contains("Sample Testcase") }.toMutableList()
                        testcases.addAll(0, problem.sampleTestcases)
                        val updatedFile = file.copy(testcases = testcases)
                        db.updateSolutionFile(updatedFile)
                    }.visibleIf(object : ComponentPredicate() {
                        override fun invoke() = db.solutionFiles[pathString]?.getLinkedProblem(db) != null

                        override fun addListener(listener: (Boolean) -> Unit) {
                            scope.launch { flow.collect { listener(invoke()) } }
                        }
                    })
                }
            }.apply {
                border = JBUI.Borders.empty(8, 8, 0, 8)
            }, BorderLayout.PAGE_START)
            add(listComponent, BorderLayout.CENTER)
        }

        Disposer.register(this, listComponent)

        testcaseListModel.addListDataListener(this)

        scope.launch {
            flow.collect {
                if (it.testcases.isItemsEqual(testcaseListModel.items)) return@collect

                resetting = true
                testcaseListModel.replaceAll(it.testcases)
                resetting = false
            }
        }

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
        scope.cancel()
    }


    private fun updateTestcases(update: (MutableList<Testcase>) -> Unit) {
        if (resetting) return
        db.solutionFiles[pathString]?.let {
            val testcases = it.testcases.toMutableList()
            update(testcases)
            db.updateSolutionFile(it.copy(testcases = testcases))
        }
    }
}



