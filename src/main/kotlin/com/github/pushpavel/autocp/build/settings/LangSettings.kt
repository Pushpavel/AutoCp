package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*

@State(
    name = "settings.Languages2",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class LangSettings : PersistentStateComponent<SerializableLangSettings> {
    val defaultLangs = buildDefaultLangs(R.others.defaultLangs)
    private val _langs = defaultLangs.toMutableMap()
    val langs: Map<String, Lang> get() = _langs

    fun updateLangs(langs: Map<String, Lang>) {
        _langs.clear()
        _langs.putAll(langs)
        ApplicationManager.getApplication().messageBus.syncPublisher(LangSettingsListener.TOPIC).langsUpdated(_langs)
    }

    override fun getState() = serializeLangSettings(langs, defaultLangs)

    override fun loadState(state: SerializableLangSettings) {
        updateLangs(deserializeLangSettings(state, defaultLangs))
    }

    companion object {
        val instance = service<LangSettings>()
    }
}

class LangNotConfiguredErr(val extension: String) : Exception()