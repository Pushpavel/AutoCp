package com.github.pushpavel.autocp.core.persistance.storables.solutions

import com.github.pushpavel.autocp.common.helpers.absoluteFrom
import com.github.pushpavel.autocp.common.helpers.relativeTo
import com.github.pushpavel.autocp.core.persistance.Storable
import com.github.pushpavel.autocp.core.persistance.storables.base.MapWithEventFlow
import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.io.path.Path
import kotlin.io.path.pathString


class Solutions(val project: Project) : MapWithEventFlow<String, Solution>(), Storable {

    fun onKey(key: String) = events.filter { it.keys.contains(key) }.map { it.map[key] }

    fun put(value: Solution) = put(value.pathString, value)

    override fun load(data: JsonObject) {
        clear()
        data.entrySet().forEach {
            val absPath = Path(it.key).absoluteFrom(project).pathString
            put(absPath, Solution.fromJson(it.value.asJsonObject))
        }
    }

    override fun save(): JsonObject {
        val json = JsonObject()
        forEach { (key, value) ->
            val relPath = Path(key).relativeTo(project).pathString
            json.add(relPath, value.toJson())
        }
        return json
    }
}