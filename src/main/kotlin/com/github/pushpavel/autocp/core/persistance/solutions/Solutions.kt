package com.github.pushpavel.autocp.core.persistance.solutions

import com.github.pushpavel.autocp.core.persistance.base.MapWithEventFlow
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Service
class Solutions(project: Project) : MapWithEventFlow<String, Solution>() {

    fun onKey(key: String) = events.filter { it.keys.contains(key) }.map { it.map[key] }

    fun put(value: Solution) = put(value.pathString, value)

    // TODO: import values from project
}