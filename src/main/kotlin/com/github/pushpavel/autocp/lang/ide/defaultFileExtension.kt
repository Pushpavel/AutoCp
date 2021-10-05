package com.github.pushpavel.autocp.lang.ide

import com.intellij.openapi.application.ApplicationInfo

val defaultFileExtensionBasedOnIDE = when (ApplicationInfo.getInstance().build.productCode) {
    // TODO: account for edu versions
    "PY", "DB" -> "py"
    "CL" -> "cpp"
    "IU", "IC", "AI" -> "java"
    "OC" -> "swift"
    "GO" -> "go"
    "PS" -> "php"
    "RD" -> "cs"
    "RM" -> "rb"
    "WS" -> "js"
    else -> "py"
}