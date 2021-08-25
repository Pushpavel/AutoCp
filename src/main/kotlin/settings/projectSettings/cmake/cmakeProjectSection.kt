package settings.projectSettings.cmake

import com.intellij.openapi.project.Project
import com.intellij.ui.layout.RowBuilder
import common.res.R

fun RowBuilder.cmakeProjectSection(project: Project) {
    val cmakeSettings = project.cmakeSettings()
    row {
        checkBox(
            R.strings.addToCMakeMsg,
            { cmakeSettings.addToCMakeLists },
            { cmakeSettings.addToCMakeLists = it }
        )
    }
}