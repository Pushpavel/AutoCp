package com.github.pushpavel.autocp.common.res

import java.util.*

object AutoCpKeys {
    const val pluginId = "com.github.pushpavel.autocp"
    const val toolWindowSelectedTabIndexKey = "$pluginId.toolWindowSelectedTabIndex"

    // Command User Macros
    const val inputPathMacro = "@in"
    const val dirPathMacro = "@dir"
    const val dirUnquotedPathMacro = "\$dir"

    // Analytics
    private val secrets = kotlin.runCatching {
        ResourceBundle.getBundle("messages.secrets")
    }.onFailure { it.printStackTrace() }.getOrNull()

    const val analyticsUserIdKey = "analyticsUserIdKey"

    // Sensitive keys
    val analyticsApiSecret = secrets?.getString("analytics_api_secrets")
    val analyticsMeasurementId = secrets?.getString("analytics_measurement_id")
}