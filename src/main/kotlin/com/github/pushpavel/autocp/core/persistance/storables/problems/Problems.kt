package com.github.pushpavel.autocp.core.persistance.storables.problems

import com.github.pushpavel.autocp.core.persistance.Storable
import com.github.pushpavel.autocp.core.persistance.storables.base.MapWithEventFlow
import com.google.gson.JsonObject

class Problems : MapWithEventFlow<Pair<String, String>, Problem>(), Storable {
    fun put(problem: Problem) = put(Pair(problem.groupName, problem.name), problem)
    override fun load(data: JsonObject) {
        clear()
        data.entrySet().forEach { g ->
            val groupName = g.key
            g.value.asJsonObject.entrySet().forEach {
                val name = it.key
                val problem = Problem.fromJson(it.value.asJsonObject)
                put(Pair(groupName, name), problem)
            }
        }
    }

    override fun save(): JsonObject {
        val data = JsonObject()
        forEach { (groupName, name), problem ->
            val group = data.get(groupName).asJsonObject ?: JsonObject()
            group.add(name, problem.toJson())
            data.add(groupName, group)
        }
        return data
    }


}