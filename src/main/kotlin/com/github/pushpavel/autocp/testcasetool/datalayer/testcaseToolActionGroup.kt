package com.github.pushpavel.autocp.testcasetool.datalayer

import com.github.pushpavel.autocp.testcasetool.components.TestcaseListPanel
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.showOkNoDialog

fun testcaseToolActionGroup(testcaseListPanel: TestcaseListPanel) = DefaultActionGroup().apply {
    val executeAction = object : AnAction("Execute", "Executes testcases", AllIcons.Actions.Execute) {
        override fun actionPerformed(e: AnActionEvent) {
            println("execute action")
        }
    }
    val deleteAllAction = object : AnAction("Remove All", "Removes all testcases", AllIcons.Actions.GC) {
        override fun actionPerformed(e: AnActionEvent) {
            val confirm =
                showOkNoDialog("Confirm Remove All Testcases", "Are you sure you want to remove all testcases ?", null)
            if (confirm)
                testcaseListPanel.model?.removeAll()
        }
    }

    add(executeAction)
    addSeparator()
    add(deleteAllAction)
}