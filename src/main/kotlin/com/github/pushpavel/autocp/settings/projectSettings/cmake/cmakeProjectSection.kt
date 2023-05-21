package com.github.pushpavel.autocp.settings.projectSettings.cmake

import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindSelected
import java.nio.file.Paths
import kotlin.io.path.exists

fun Panel.cmakeProjectSection(project: Project) {
    val cmakeSettings = project.cmakeSettings()
    val cmakeFile = Paths.get(project.basePath!!, ".autocp")
    if (ApplicationInfo.getInstance().build.productCode == "CL" || cmakeFile.exists())
        row {
            checkBox(R.strings.addToCMakeMsg).bindSelected(
                { cmakeSettings.addToCMakeLists },
                { cmakeSettings.addToCMakeLists = it }
            )
        }
}