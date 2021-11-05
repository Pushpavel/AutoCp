package com.github.pushpavel.autocp.core.persistance.testcases

import com.github.pushpavel.autocp.core.persistance.base.MapWithEventFlow
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionListModel
import kotlinx.coroutines.flow.*

@Service
class Testcases(project: Project) : MapWithEventFlow<String, CollectionListModel<Testcase>>() {

    fun onSolutionKey(solutionPathString: String): Flow<CollectionListModel<Testcase>?> {
        return flowOf(this[solutionPathString]).onCompletion {
            emitAll(events.filter { it.keys.contains(solutionPathString) }.map { it.map[solutionPathString] })
        }
    }

    // TODO: maintain consistency
    // TODO: import values from project
}