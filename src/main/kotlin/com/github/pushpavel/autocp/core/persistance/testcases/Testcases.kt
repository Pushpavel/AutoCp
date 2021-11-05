package com.github.pushpavel.autocp.core.persistance.testcases

import com.github.pushpavel.autocp.core.persistance.base.Table
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service
class Testcases(project: Project) : Table<Testcase.Key, Testcase>() {
    override fun getKey(value: Testcase) = value.getKey()

    // TODO: maintain consistency
    // TODO: import values from project
}