package com.github.pushpavel.autocp.core.persistance.storables.testcases

import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

@Serializable // TODO: remove Serializable after refactoring
data class Testcase(
    val num: Int,
    val input: String,
    val output: String,
) {
    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("num", num)
        json.addProperty("input", input)
        json.addProperty("output", output)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): Testcase {
            val num = json.get("num").asInt
            val input = json.get("input").asString
            val output = json.get("output").asString
            return Testcase(num, input, output)
        }
    }
}