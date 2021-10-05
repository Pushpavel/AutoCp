package com.github.pushpavel.autocp.settings.generalSettings

import com.intellij.openapi.components.*

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "settings.General",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpGeneralSettings : PersistentStateComponent<AutoCpGeneralSettings> {
    var openFilesOnGather = OpenFileOnGather.ONLY_FIRST

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        openFilesOnGather = state.openFilesOnGather
    }

    companion object {
        val instance: AutoCpGeneralSettings get() = service()
    }
}

enum class OpenFileOnGather {
    NONE,
    ONLY_FIRST,
    ALL
}