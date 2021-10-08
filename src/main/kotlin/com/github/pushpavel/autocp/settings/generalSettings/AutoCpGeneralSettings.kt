package com.github.pushpavel.autocp.settings.generalSettings

import com.github.pushpavel.autocp.common.res.R
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
    var fileGenerationRoot = "\$groupName"


    fun constructFileGenerationRoot(groupName: String): String {
        return fileGenerationRoot.replace(R.keys.groupNameMacro, groupName)
    }

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        openFilesOnGather = state.openFilesOnGather
        fileGenerationRoot = state.fileGenerationRoot
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