package com.github.pushpavel.autocp.testcasetool.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

//TODO: Not implemented
class ExecuteAction : AnAction("Execute", "Executes the action", AllIcons.Actions.Execute) {
    override fun actionPerformed(e: AnActionEvent) {
        println("execute action")
    }
}