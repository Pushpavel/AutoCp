package com.github.pushpavel.autocp.core.persistance.storables.problems

import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcase
import com.google.gson.JsonArray
import com.google.gson.JsonObject


data class Problem(
    val name: String,
    val groupName: String,
    val url: String,
    val sampleTestcases: List<Testcase>,
    val memoryLimit: Long,
    val timeLimit: Long,
) {
    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("name", name)
        json.addProperty("groupName", groupName)
        json.addProperty("url", url)
        json.add("sampleTestcases", JsonArray().apply {
            sampleTestcases.forEach { add(it.toJson()) }
        })
        json.addProperty("memoryLimit", memoryLimit)
        json.addProperty("timeLimit", timeLimit)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): Problem {
            val name = json.get("name").asString
            val groupName = json.get("groupName").asString
            val url = json.get("url").asString
            val sampleTestcases = json.get("sampleTestcases").asJsonArray.map { Testcase.fromJson(it.asJsonObject) }
            val memoryLimit = json.get("memoryLimit").asLong
            val timeLimit = json.get("timeLimit").asLong
            return Problem(name, groupName, url, sampleTestcases, memoryLimit, timeLimit)
        }
    }
}