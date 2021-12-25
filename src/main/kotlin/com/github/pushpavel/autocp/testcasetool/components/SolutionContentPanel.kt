package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.helpers.DisposableScope
import com.github.pushpavel.autocp.common.helpers.doDisposal
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.github.pushpavel.autocp.core.persistance.storable
import com.github.pushpavel.autocp.core.persistance.storables.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcase
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcases
import com.github.pushpavel.autocp.testcasetool.actions.actionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.ui.AncestorListenerAdapter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import com.intellij.util.ui.components.BorderLayoutPanel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
import javax.swing.event.AncestorEvent

class SolutionContentPanel(project: Project, val toolWindow: ToolWindow) : BorderLayoutPanel() {

    private val testcaseListPanel = TestcaseListPanel()
    private val testcaseListContainer = panel(LCFlags.fillX) {
        row {
            scrollPane(
                BorderLayoutPanel().apply {
                    add(testcaseListPanel, BorderLayout.NORTH)
                }
            ).applyToComponent {
                horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
                border = BorderFactory.createLoweredSoftBevelBorder()
            }
        }
        row {

            button("New Testcase") {
                // add new testcase to current model
                testcaseListPanel.model?.let { model ->
                    // get max value of num in testcases + 1
                    val maxNum = (model.items.map { it.num }.maxOrNull() ?: 0) + 1
                    model.add(Testcase(maxNum, "input", "output"))
                }
            }
            JBLabel().apply {
                icon = R.icons.clock
                text = "1000ms"
            }()
        }
        // Workaround to display small gap
        row {
            JBLabel()()
        }
    }
    private val testcases = project.storable<Testcases>()

    private val scope = mainScope()
    private val solutionScoped by DisposableScope()

    var solution: Solution? by setter(null) { solution ->
        solutionScoped.doDisposal()
        this.value = solution
        if (solution == null) return@setter

        // update testcase model
        val job = scope.launch { testcases.onSolutionKey(solution.pathString).collect { testcaseListPanel.model = it } }
        Disposer.register(solutionScoped) {
            job.cancel()
            testcaseListPanel.model = null
        }
    }

    init {
        val actionContainer = ActionContainer(actionGroup(), testcaseActionGroup(), testcaseListContainer)
        add(actionContainer)

        addAncestorListener(object : AncestorListenerAdapter() {
            override fun ancestorAdded(event: AncestorEvent?) {
                actionContainer.bottomAnchored = toolWindow.anchor == ToolWindowAnchor.BOTTOM
            }
        })
    }
}