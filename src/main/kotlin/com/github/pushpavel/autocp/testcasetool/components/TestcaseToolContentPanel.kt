package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.helpers.defaultScope
import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.github.pushpavel.autocp.core.persistance.solutions.Solution
import com.github.pushpavel.autocp.core.persistance.testcases.Testcases
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TestcaseToolContentPanel(project: Project, val toolWindow: ToolWindow, solution: Solution) {

    val testcaseListPanel = TestcaseListPanel()
    val testcases = project.service<Testcases>()
    var solutionScope = defaultScope()

    var solution: Solution by setter(solution) {
        // cancel previous scope
        solutionScope.cancel(); solutionScope = defaultScope()

        solutionScope.launch {
            testcases.onSolutionKey(solution.pathString).collect { testcaseListPanel.model = it }
        }.invokeOnCompletion { testcaseListPanel.model = null }
    }
}