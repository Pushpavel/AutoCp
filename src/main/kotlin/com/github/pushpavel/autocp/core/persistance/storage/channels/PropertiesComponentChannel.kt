package com.github.pushpavel.autocp.core.persistance.storage.channels

import com.github.pushpavel.autocp.common.helpers.properties
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.core.persistance.storage.StorageChannel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.openapi.project.Project

class PropertiesComponentChannel : StorageChannel {
    override fun save(project: Project, data: Map<String, JsonObject>) {
        val properties = project.properties
        data.forEach { (key, value) -> properties.setValue(R.keys.pluginId + key, value.toString()) }
    }

    override fun load(project: Project, keys: List<String>): Map<String, JsonObject> {
        val properties = project.properties
        val map = mutableMapOf<String, JsonObject>()

        for (key in keys) {
            val value = properties.getValue(R.keys.pluginId + key)
            if (value != null) map[key] = JsonParser.parseString(value).asJsonObject
        }

        return map
    }
}