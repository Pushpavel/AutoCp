package com.github.pushpavel.autocp.testcasetool.actions

import com.intellij.openapi.actionSystem.DefaultActionGroup

fun actionGroup() = DefaultActionGroup().apply {
    add(ExecuteAction())
}