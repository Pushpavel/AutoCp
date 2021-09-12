package com.github.pushpavel.autocp.common.analytics

import com.github.pushpavel.autocp.common.helpers.analyticsClientId
import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import io.ktor.client.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

@Service
class GoogleAnalytics {

    private val scope = ioScope()

    private val httpClient = HttpClient {
        expectSuccess = false
        ResponseObserver { response ->
            println("HTTP status: ${response.status.value}\n${response.readText()}")
        }
    }

    fun sendEvent(event: Event) {
        scope.launch {
            httpClient.post<String>("${R.keys.analyticsEndPoint}?measurement_id=${R.keys.analyticsMeasurementId}&api_secret=${R.keys.analyticsApiSecret}") {
                contentType(ContentType.Application.Json)
                body = buildJsonObject {
                    put("client_id", PropertiesComponent.getInstance().analyticsClientId)
                    put("non_personalized_ads", true)
                    putJsonArray("events") {
                        addJsonObject { event.buildJsonObj(this) }
                    }
                }.toString().encodeToByteArray()
            }
        }
    }
}

fun Project.analytics(): GoogleAnalytics = service()