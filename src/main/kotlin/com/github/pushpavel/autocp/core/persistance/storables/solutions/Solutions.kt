package com.github.pushpavel.autocp.core.persistance.storables.solutions

import com.github.pushpavel.autocp.core.persistance.storables.base.MapWithEventFlow
import com.github.pushpavel.autocp.core.persistance.Storable
import com.google.gson.JsonObject
import com.intellij.openapi.components.Service
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Service
class Solutions : MapWithEventFlow<String, Solution>(), Storable {

    fun onKey(key: String) = events.filter { it.keys.contains(key) }.map { it.map[key] }

    fun put(value: Solution) = put(value.pathString, value)

    override fun load(data: JsonObject) {
        clear()
        data.entrySet().forEach {
            put(it.key, Solution.fromJson(it.value.asJsonObject))
        }
    }

    override fun save(): JsonObject {
        val json = JsonObject()
        forEach { (key, value) -> json.add(key, value.toJson()) }
        return json
    }
}