package com.github.pushpavel.autocp.common.analytics.events

import com.github.pushpavel.autocp.common.analytics.Event
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class UninstallEvent(val version: String) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "uninstall")
        putJsonObject("params") {
            put("uninstalledVersion", version)
        }
    }
}