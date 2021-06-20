package database.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.sqldelight.ColumnAdapter

class DataColumnAdapter : ColumnAdapter<JsonObject, String> {
    private val gson by lazy { Gson() }
    override fun decode(databaseValue: String): JsonObject {
        return gson.fromJson(databaseValue, JsonObject::class.java)
    }

    override fun encode(value: JsonObject): String {
        return value.toString()
    }
}