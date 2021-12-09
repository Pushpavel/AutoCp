package com.github.pushpavel.autocp.core.persistance.storage

import com.google.gson.JsonObject

/**
 * interface to define an entity that can be stored
 */
interface Storable {
    val id: String
    fun load(data: JsonObject)
    fun save(): JsonObject
}