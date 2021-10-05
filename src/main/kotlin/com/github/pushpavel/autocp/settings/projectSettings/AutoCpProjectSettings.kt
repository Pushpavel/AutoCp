package com.github.pushpavel.autocp.settings.projectSettings

import com.github.pushpavel.autocp.lang.ide.defaultFileExtensionBasedOnIDE
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "settings.Project",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpProjectSettings : PersistentStateComponent<AutoCpProjectSettings> {

    var defaultFileExtension = defaultFileExtensionBasedOnIDE

    override fun getState() = this
    override fun loadState(state: AutoCpProjectSettings) {
        defaultFileExtension = state.defaultFileExtension
    }
}

fun Project.autoCpProject(): AutoCpProjectSettings = service()