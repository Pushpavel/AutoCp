package com.github.pushpavel.autocp.core.persistance

import com.google.gson.JsonObject

/**
 * interface to define an entity that can be stored
 */
interface Storable {
    fun load(data: JsonObject)
    fun save(): JsonObject
}