package com.github.pushpavel.autocp.common.analytics.events

import com.github.pushpavel.autocp.common.analytics.Event
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class BatchGatherEvent(val groupName: String, val extension: String, val problemCount: Int) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "batchGather")
        putJsonObject("params") {
            val grp = groupName.split('-')
            put("site", grp[0].trim())
            val category = grp.getOrNull(1)?.trim()
            if (category != null)
                put("category", category)

            put("fileExtension", extension)
            put("problemCount", problemCount)
        }
    }
}