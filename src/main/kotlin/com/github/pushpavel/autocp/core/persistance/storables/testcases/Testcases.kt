package com.github.pushpavel.autocp.core.persistance.storables.testcases

import com.github.pushpavel.autocp.core.persistance.storables.base.MapWithEventFlow
import com.github.pushpavel.autocp.core.persistance.Storable
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.components.Service
import com.intellij.ui.CollectionListModel
import kotlinx.coroutines.flow.*

@Service
class Testcases : MapWithEventFlow<String, CollectionListModel<Testcase>>(), Storable {

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
            put(it.key, CollectionListModel(
                it.value.asJsonArray.map { json -> Testcase.fromJson(json.asJsonObject) }
            ))
        }
    }

    override fun save(): JsonObject {
        val json = JsonObject()
        forEach { (key, value) ->
            val array = JsonArray()
            value.items.forEach { array.add(it.toJson()) }
            json.add(key, array)
        }
        return json
    }

    // TODO: maintain consistency
    // TODO: import values from project
}