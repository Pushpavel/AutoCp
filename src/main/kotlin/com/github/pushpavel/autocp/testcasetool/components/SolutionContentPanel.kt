package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.helpers.DisposableScope
import com.github.pushpavel.autocp.common.helpers.doDisposal
import com.github.pushpavel.autocp.common.helpers.mainScope
import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.github.pushpavel.autocp.core.persistance.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.testcases.Testcases
import com.github.pushpavel.autocp.testcasetool.actions.actionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.ui.AncestorListenerAdapter
import com.intellij.util.ui.components.BorderLayoutPanel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.swing.event.AncestorEvent

class SolutionContentPanel(project: Project, val toolWindow: ToolWindow) : BorderLayoutPanel() {

    private val testcaseListPanel = TestcaseListPanel()
    private val testcases = project.service<Testcases>()

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
        val actionContainer = ActionContainer(actionGroup(), testcaseActionGroup(), testcaseListPanel)
        add(actionContainer)

        addAncestorListener(object : AncestorListenerAdapter() {
            override fun ancestorAdded(event: AncestorEvent?) {
                actionContainer.bottomAnchored = toolWindow.anchor == ToolWindowAnchor.BOTTOM
            }
        })
    }
}