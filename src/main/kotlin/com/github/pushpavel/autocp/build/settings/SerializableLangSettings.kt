package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.Lang

data class SerializableLangSettings(
    var langs: Map<String, SerializableLang> = mapOf()
)

data class SerializableLang(
    var extension: String = "",
    var buildCommand: String? = null,
    var executeCommand: String = "",
)

fun serializeLangSettings(langs: Map<String, Lang>, defaultLangs: Map<String, Lang>): SerializableLangSettings {
    return SerializableLangSettings(
        langs = langs.filter {
            if (!defaultLangs.containsKey(it.key)) return@filter true
            val d = defaultLangs[it.key]!!
            return@filter d.executeCommand != it.value.executeCommand || d.buildCommand != it.value.buildCommand
        }.mapValues {
            SerializableLang(
                it.value.extension,
                it.value.buildCommand,
                it.value.executeCommand,
            )
        }
    )
}

fun deserializeLangSettings(settings: SerializableLangSettings, defaultLangs: Map<String, Lang>): MutableMap<String, Lang> {
    val langs = defaultLangs.toMutableMap()
    langs.putAll(settings.langs.mapValues {
        Lang(
            it.value.extension,
            it.value.buildCommand,
            it.value.executeCommand,
            defaultLangs.containsKey(it.key)
        )
    })
    return langs
}