package com.github.pushpavel.autocp.settings.projectSettings

import com.github.pushpavel.autocp.settings.projectSettings.cmake.cmakeProjectSection
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel

class AutoCpProjectSettingsConfigurable(val project: Project) : BoundConfigurable("Project") {
    private val projectSettings = project.autoCpProject()
    var extension = ""

    override fun createPanel() = panel {
        titledRow("Solution File Generation") {
            row("File Extension") {
                textField(::extension, 2)
                    .onReset { extension = projectSettings.defaultFileExtension }
                    .onIsModified { extension != projectSettings.defaultFileExtension }
                        // TODO: validate
                    .onApply {
                        // TODO: adjust leading dot
                        if (extension.isNotBlank())
                            projectSettings.defaultFileExtension = extension.trim()
                    }
            }
            cmakeProjectSection(project)
        }
    }

}
