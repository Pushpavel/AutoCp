package com.github.pushpavel.autocp.settings.generalSettings

import com.github.pushpavel.autocp.lang.settings.preferredLangBasedOnIDE
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import com.intellij.openapi.components.*
import com.jetbrains.rd.util.firstOrNull

/**
 * Model class for Settings of the plugin that persists across restarts
 */
@State(
    name = "settings.General",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpGeneralSettings : PersistentStateComponent<AutoCpGeneralSettings> {
    // TODO: set this default based on IDE
    var preferredLangId: String? =
        AutoCpLangSettings.instance.languages.let { preferredLangBasedOnIDE(it) ?: it.firstOrNull()?.value }?.langId
    var shouldStartGatheringOnStart = true
    var openFilesOnGather = OpenFileOnGather.ONLY_FIRST

    fun getPreferredLang(): Lang? {
        return AutoCpLangSettings.instance.languages.run { this[preferredLangId] ?: firstOrNull()?.value }
    }

    override fun getState() = this

    override fun loadState(state: AutoCpGeneralSettings) {
        preferredLangId = state.preferredLangId
        shouldStartGatheringOnStart = state.shouldStartGatheringOnStart
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