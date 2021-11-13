package com.github.pushpavel.autocp.extend.ide

import com.intellij.openapi.application.ApplicationInfo

val defaultFileExtensionBasedOnIDE = when (ApplicationInfo.getInstance().build.productCode) {
    "PY", "PCA", "PC", "PYA", "PE", "DB", "PD" -> "py"
    "CL" -> "cpp"
    "IU", "IC", "IE", "AI" -> "java"
    "MPS" -> "c"
    "OC" -> "swift"
    "GO" -> "go"
    "PS" -> "php"
    "RD" -> "cs"
    "RM" -> "rb"
    "WS" -> "js"
    else -> "py"
}