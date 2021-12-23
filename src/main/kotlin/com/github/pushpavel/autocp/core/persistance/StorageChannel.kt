package com.github.pushpavel.autocp.core.persistance

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project

/**
 * save and load data on a particular location by this class
 * see [StorageManager]
 */
interface StorageChannel {
    fun save(project: Project, data: Map<String, JsonObject>)
    fun load(project: Project, keys: List<String>): Map<String, JsonObject> = emptyMap()
}