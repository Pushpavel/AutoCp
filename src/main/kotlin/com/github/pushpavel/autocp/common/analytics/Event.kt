package com.github.pushpavel.autocp.common.analytics

import kotlinx.serialization.json.JsonObjectBuilder

interface Event {
    fun JsonObjectBuilder.buildJson()
    fun buildJsonObj(builder: JsonObjectBuilder) = builder.buildJson()
}