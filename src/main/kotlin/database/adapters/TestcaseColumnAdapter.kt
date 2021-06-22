package database.adapters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.util.containers.OrderedSet
import com.squareup.sqldelight.ColumnAdapter
import database.models.Testcase

class TestcaseColumnAdapter : ColumnAdapter<OrderedSet<Testcase>, String> {
    private val gson by lazy { Gson() }
    private val type = object : TypeToken<OrderedSet<Testcase>>() {}.type

    override fun decode(databaseValue: String): OrderedSet<Testcase> {
        return gson.fromJson(databaseValue, type)
    }

    override fun encode(value: OrderedSet<Testcase>): String {
        return gson.toJson(value)
    }
}