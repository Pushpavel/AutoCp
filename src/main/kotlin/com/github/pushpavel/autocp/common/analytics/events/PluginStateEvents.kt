package com.github.pushpavel.autocp.common.analytics.events

import com.github.pushpavel.autocp.common.analytics.Event
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

data class InstallEvent(val version: String) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "install")
        putJsonObject("params") {
            put("installedVersion", version)
        }
    }
}

class UninstallEvent(val version: String) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "uninstall")
        putJsonObject("params") {
            put("uninstalledVersion", version)
        }
    }
}

class UpdateEvent(val version: String) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "update")
        putJsonObject("params") {
            put("updatedVersion", version)
        }
    }
}
