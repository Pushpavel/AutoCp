package com.github.pushpavel.autocp.tool.ui

import com.github.pushpavel.autocp.common.helpers.UniqueNameEnforcer
import com.github.pushpavel.autocp.common.helpers.isItemsEqual
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.swing.editableList.EditableListView
import com.github.pushpavel.autocp.database.SolutionFiles
import com.github.pushpavel.autocp.database.models.Testcase
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.BorderLayout
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import javax.swing.JComponent
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class TestcaseListPanel(project: Project, private val pathString: String) : Disposable, ListDataListener {

    private val scope = mainScope()
    private val solutionFiles = SolutionFiles.getInstance(project)
    private val flow = solutionFiles.listenFlow(pathString).filterNotNull()
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

        val copyAllAction = object : DumbAwareAction("Copy All Testcases", null, AllIcons.Actions.Copy) {
            override fun actionPerformed(e: AnActionEvent) {
                val json = Json.encodeToString(testcaseListModel.items.toList())
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(json), null)
            }
        }

        val pasteAction = object : DumbAwareAction("Paste Testcase(s)", null, AllIcons.Actions.Download) {
            override fun actionPerformed(e: AnActionEvent) {
                val text = runCatching {
                    Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as? String
                }.getOrNull() ?: return
                val list = runCatching { Json.decodeFromString<List<Testcase>>(text) }.getOrNull()
                    ?: runCatching { listOf(Json.decodeFromString<Testcase>(text)) }.getOrNull()
                    ?: return
                list.forEach { testcaseListModel.add(it.copy(name = testcaseNameEnforcer.buildUniqueName(it.name))) }
            }
        }

        val resetAction = object : DumbAwareAction("Reset Sample Testcases", null, AllIcons.Actions.Refresh) {
            override fun actionPerformed(e: AnActionEvent) {
                val file = solutionFiles[pathString]
                val problem = file?.getLinkedProblem(project)
                if (file == null || problem == null) return
                val testcases = file.testcases.filter { !it.name.contains("Sample Testcase") }.toMutableList()
                testcases.addAll(0, problem.sampleTestcases)
                solutionFiles.update(file.copy(testcases = testcases))
            }

            override fun update(e: AnActionEvent) {
                e.presentation.isVisible = solutionFiles[pathString]?.getLinkedProblem(project) != null
            }

            override fun getActionUpdateThread() = ActionUpdateThread.EDT
        }

        val popupGroup = DefaultActionGroup(copyAllAction, pasteAction, resetAction).apply {
            isPopup = true
            templatePresentation.text = "Testcase Actions"
            templatePresentation.icon = AllIcons.General.GearPlain
        }

        component = BorderLayoutPanel().apply {
            add(panel {
                row {
                    label("Constraints:")
                    label("").applyToComponent {
                        icon = R.icons.clock
                        scope.launch {
                            flow.collect {
                                text = "${it.judgeSettings.timeLimit}ms"
                            }
                        }
                    }
                    placeholder()

                    cell(
                        ActionManager.getInstance()
                            .createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, popupGroup, true)
                            .apply { targetComponent = null }
                            .component
                    )
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
        solutionFiles[pathString]?.let {
            val testcases = it.testcases.toMutableList()
            update(testcases)
            solutionFiles.update(it.copy(testcases = testcases))
        }
    }
}



