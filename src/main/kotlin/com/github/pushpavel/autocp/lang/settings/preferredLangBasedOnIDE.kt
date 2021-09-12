package com.github.pushpavel.autocp.lang.settings

import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import com.intellij.openapi.application.ApplicationInfo

fun preferredLangBasedOnIDE(langs: Map<String, Lang>) = langs[preferredLangId]

// TODO: include all supported IDEs
val preferredLangId = when (ApplicationInfo.getInstance().build.productCode) {
    "PY" -> "Python"
    "CL" -> "ObjectiveC"
    "IU", "IC" -> "Java"
    else -> null
}