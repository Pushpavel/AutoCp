package com.github.pushpavel.autocp.common.analytics

import com.github.pushpavel.autocp.common.helpers.analyticsClientId
import com.github.pushpavel.autocp.common.helpers.ioScope
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.Service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.SystemInfo
import io.ktor.client.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

@Service
class GoogleAnalytics {

    private val scope = ioScope()

    private val httpClient = HttpClient {
        expectSuccess = false
        ResponseObserver { response ->
            println("HTTP status: ${response.status.value}\n${response.bodyAsText()}")
        }
    }

    fun sendEvent(event: Event) {
        scope.launch {
            httpClient.post("${R.keys.analyticsEndPoint}?measurement_id=${R.keys.analyticsMeasurementId}&api_secret=${R.keys.analyticsApiSecret}") {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    put("client_id", PropertiesComponent.getInstance().analyticsClientId)
                    putJsonObject("user_properties") {
                        putJsonObject("os") {
                            put("value", SystemInfo.getOsNameAndVersion())
                        }
                        putJsonObject("productCode") {
                            put("value", ApplicationInfo.getInstance().build.productCode)
                        }
                        putJsonObject("productVersion") {
                            put("value", ApplicationInfo.getInstance().build.asStringWithoutProductCodeAndSnapshot())
                        }
                        putJsonObject("version") {
                            put("value", PluginManagerCore.getPlugin(PluginId.getId(R.keys.pluginId))?.version)
                        }
                    }
                    put("non_personalized_ads", true)
                    putJsonArray("events") {
                        addJsonObject { event.buildJsonObj(this) }
                    }
                }.toString().also { println(it) }.encodeToByteArray())
            }
        }
    }

    companion object {
        val instance = GoogleAnalytics()
    }
}