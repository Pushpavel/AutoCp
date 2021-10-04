package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.res.R
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "settings.Languages2",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class LangSettings : PersistentStateComponent<SerializableLangSettings> {
    private val defaultLangs = buildDefaultLangs(R.others.defaultLangs)
    private val _langs = defaultLangs.toMutableMap()
    val langs: Map<String, Lang> get() = _langs

    override fun getState() = serializeLangSettings(langs, defaultLangs)

    override fun loadState(state: SerializableLangSettings) {
        _langs.clear()
        _langs.putAll(deserializeLangSettings(state, defaultLangs))
    }
}