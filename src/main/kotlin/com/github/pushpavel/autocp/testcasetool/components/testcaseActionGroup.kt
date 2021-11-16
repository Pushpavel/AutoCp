package com.github.pushpavel.autocp.testcasetool.components

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

fun testcaseActionGroup() = DefaultActionGroup().apply {
    val successAction = object : AnAction("Success", "Success Action", AllIcons.RunConfigurations.TestPassed) {
        override fun actionPerformed(e: AnActionEvent) {
            println("testcase Passed")
        }
    }

    val failAction = object : AnAction("Failure", "Failure Action", AllIcons.RunConfigurations.TestFailed) {
        override fun actionPerformed(e: AnActionEvent) {
            println("testcase fAILED")
        }
    }

    add(successAction)
    add(failAction)
}