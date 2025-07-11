package com.github.pushpavel.autocp.common.res

import java.util.*

object AutoCpKeys {
    const val pluginId = "com.github.pushpavel.autocp"
    const val toolWindowSelectedTabIndexKey = "$pluginId.toolWindowSelectedTabIndex"
    const val autoCpFileVersionNumber = 2

    // Ids
    const val autoCpToolWindowId = "Testcases"
    const val autoCpExecutorId = "TestcasesExecutor"

    // Command User Macros
    const val inputPathMacro = "@in"
    const val dirPathMacro = "@dir"
    const val dirUnquotedPathMacro = "\$dir"

    // Other Macros
    const val groupNameMacro = "\$groupName"

    // File Template Pre-defined variables
    const val problemNameVar = "PROBLEM_NAME"
    const val groupNameVar = "PROBLEM_GROUP_NAME"
    const val onlineJudgeVar = "ONLINE_JUDGE_NAME"

    // File template keys
    const val fileTemplateName = "CP_TEMPLATE"

    // Analytics
    const val analyticsEndPoint = "https://www.google-analytics.com/mp/collect"
    const val analyticsClientIdKey = "$pluginId.analyticsClientIdKey"
    private val secrets = kotlin.runCatching {
        ResourceBundle.getBundle("messages.secrets")
    }.onFailure { it.printStackTrace() }.getOrNull()


    // Sensitive keys
    val analyticsApiSecret = secrets?.getString("analytics_api_secrets")
    val analyticsMeasurementId = secrets?.getString("analytics_measurement_id")
}