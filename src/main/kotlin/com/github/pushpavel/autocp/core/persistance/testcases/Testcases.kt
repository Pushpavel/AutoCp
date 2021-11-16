package com.github.pushpavel.autocp.core.persistance.testcases

import com.github.pushpavel.autocp.core.persistance.base.MapWithEventFlow
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionListModel
import kotlinx.coroutines.flow.*

@Service
class Testcases(project: Project) : MapWithEventFlow<String, CollectionListModel<Testcase>>() {

    fun onSolutionKey(solutionPathString: String): Flow<CollectionListModel<Testcase>?> {
        val currentValue = getOrPut(solutionPathString)
        return flowOf<CollectionListModel<Testcase>?>(currentValue).onCompletion {
            emitAll(events.filter { it.keys.contains(solutionPathString) }.map { it.map[solutionPathString] })
        }
    }

    fun getOrPut(solutionPathString: String): CollectionListModel<Testcase> {
        return this[solutionPathString] ?: CollectionListModel<Testcase>().also { put(solutionPathString, it) }
    }

    // TODO: maintain consistency
    // TODO: import values from project
}