package com.github.pushpavel.autocp.settings.projectSettings.cmake

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "settings.Project.CMake",
    storages = [Storage("autoCpCMakeSettings.xml")]
)
@Service
class CMakeSettings : PersistentStateComponent<CMakeSettings> {
    var addToCMakeLists = true

    override fun getState() = this

    override fun loadState(state: CMakeSettings) {
        addToCMakeLists = state.addToCMakeLists
    }
}

fun Project.cmakeSettings(): CMakeSettings = service()