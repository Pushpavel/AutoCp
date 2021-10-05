package com.github.pushpavel.autocp.build.settings

import com.github.pushpavel.autocp.build.Lang
import com.intellij.util.messages.Topic

interface LangSettingsListener {
    companion object {
        val TOPIC = Topic.create("Lang Settings Listener", LangSettingsListener::class.java)
    }

    fun langsUpdated(langs: Map<String, Lang>)
}