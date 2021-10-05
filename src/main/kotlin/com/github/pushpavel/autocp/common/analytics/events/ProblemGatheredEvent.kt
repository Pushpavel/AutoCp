package com.github.pushpavel.autocp.common.analytics.events

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.analytics.Event
import com.github.pushpavel.autocp.database.models.Problem
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

data class ProblemGatheredEvent(
    val problem: Problem,
    val lang: Lang
) : Event {
    override fun JsonObjectBuilder.buildJson() {
        put("name", "problemGathered")
        putJsonObject("params") {
            put("url", problem.url)
            put("site", problem.groupName.split('-')[0].trim())
            val category = problem.groupName.split('-').getOrNull(1)?.trim()
            if (category != null)
                put("category", category)

            put("fileExtension", lang.extension)
        }
    }

}
