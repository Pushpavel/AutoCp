package com.github.pushpavel.autocp.settings.projectSettings

import com.github.pushpavel.autocp.lang.settings.preferredLangBasedOnIDE
import com.github.pushpavel.autocp.settings.generalSettings.AutoCpGeneralSettings
import com.github.pushpavel.autocp.settings.langSettings.AutoCpLangSettings
import com.github.pushpavel.autocp.settings.langSettings.model.Lang
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.firstOrNull

@State(
    name = "settings.Project",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpProjectSettings : PersistentStateComponent<AutoCpProjectSettings> {

    var preferredLangId: String? =
        AutoCpLangSettings.instance.languages.let { preferredLangBasedOnIDE(it) ?: it.firstOrNull()?.value }?.langId

    var overridePreferredLang = false

    var shouldStartGatheringOnStart = true
    var overrideShouldStartGatheringOnStart = false

    fun guessPreferredLang(): Lang? {
        if (!overridePreferredLang)
            return AutoCpGeneralSettings.instance.getPreferredLang()

        return AutoCpLangSettings.instance.languages.run { this[preferredLangId] ?: firstOrNull()?.value }
    }

    fun shouldStartGatheringOnStart(): Boolean {
        if (!overrideShouldStartGatheringOnStart)
            return AutoCpGeneralSettings.instance.shouldStartGatheringOnStart

        return shouldStartGatheringOnStart
    }

    override fun getState() = this
    override fun loadState(state: AutoCpProjectSettings) {
        preferredLangId = state.preferredLangId
        overridePreferredLang = state.overridePreferredLang
        shouldStartGatheringOnStart = state.shouldStartGatheringOnStart
        overrideShouldStartGatheringOnStart = state.overrideShouldStartGatheringOnStart
    }
}

fun Project.autoCpProject(): AutoCpProjectSettings = service()