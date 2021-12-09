package com.github.pushpavel.autocp.core.persistance.storage

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project

/**
 * Processes the data for schema changes and applies fixes for it
 */
interface StorableProcessor {
    fun process(project: Project, data: MutableMap<String, JsonObject>)
}