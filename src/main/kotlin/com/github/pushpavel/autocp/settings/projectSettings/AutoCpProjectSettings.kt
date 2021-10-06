package com.github.pushpavel.autocp.settings.projectSettings

import com.github.pushpavel.autocp.extend.ide.defaultFileExtensionBasedOnIDE
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "settings.Project",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpProjectSettings : PersistentStateComponent<AutoCpProjectSettings> {

    var defaultFileExtension = defaultFileExtensionBasedOnIDE
    var askBeforeFileGeneration = true

    override fun getState() = this
    override fun loadState(state: AutoCpProjectSettings) {
        defaultFileExtension = state.defaultFileExtension
        askBeforeFileGeneration = state.askBeforeFileGeneration
    }
}

fun Project.autoCpProject(): AutoCpProjectSettings = service()