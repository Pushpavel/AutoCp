package com.github.pushpavel.autocp.core.persistance.storables.testcases

import com.github.pushpavel.autocp.common.helpers.absoluteFrom
import com.github.pushpavel.autocp.common.helpers.relativeTo
import com.github.pushpavel.autocp.core.persistance.Storable
import com.github.pushpavel.autocp.core.persistance.storables.base.MapWithEventFlow
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionListModel
import kotlinx.coroutines.flow.*
import kotlin.io.path.Path
import kotlin.io.path.pathString

class Testcases(val project: Project) : MapWithEventFlow<String, CollectionListModel<Testcase>>(), Storable {

    fun onSolutionKey(solutionPathString: String): Flow<CollectionListModel<Testcase>?> {
        val currentValue = getOrPut(solutionPathString)
        return flowOf<CollectionListModel<Testcase>?>(currentValue).onCompletion {
            emitAll(events.filter { it.keys.contains(solutionPathString) }.map { it.map[solutionPathString] })
        }
    }

    fun getOrPut(solutionPathString: String): CollectionListModel<Testcase> {
        return this[solutionPathString] ?: CollectionListModel<Testcase>().also { put(solutionPathString, it) }
    }

    override fun load(data: JsonObject) {
        clear()
        data.entrySet().forEach {
            val absPath = Path(it.key).absoluteFrom(project).pathString
            put(absPath, CollectionListModel(
                it.value.asJsonArray.map { json -> Testcase.fromJson(json.asJsonObject) }
            ))
        }
    }

    override fun save(): JsonObject {
        val json = JsonObject()
        forEach { (key, value) ->
            val array = JsonArray()
            value.items.forEach { array.add(it.toJson()) }

            val relPath = Path(key).relativeTo(project).pathString
            json.add(relPath, array)
        }
        return json
    }

    // TODO: maintain consistency
    // TODO: import values from project
}