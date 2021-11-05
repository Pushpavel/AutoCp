package com.github.pushpavel.autocp.core.persistance.solutions

import com.github.pushpavel.autocp.core.persistance.base.Table
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project


@Service
class Solutions(project: Project) : Table<String, Solution>() {
    override fun getKey(value: Solution) = value.pathString

    // TODO: maintain consistency
    // TODO: import values from project
}