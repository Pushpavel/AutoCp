package database.adapters

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.sqldelight.ColumnAdapter

/**
 * [ColumnAdapter] for data field in Problem Table
 * this JsonObject contains properties of Problem not currently utilized in the plugin.
 */
class DataColumnAdapter : ColumnAdapter<JsonObject, String> {
    private val gson by lazy { Gson() }
    override fun decode(databaseValue: String): JsonObject {
        return gson.fromJson(databaseValue, JsonObject::class.java)
    }

    override fun encode(value: JsonObject): String {
        return value.toString()
    }
}