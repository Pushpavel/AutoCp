package com.github.pushpavel.autocp.common.listeners

import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.UninstallEvent
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor

class AutoCpPluginListener : DynamicPluginListener {

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        println("AutoCp Plugin Unloading... isUpdate:$isUpdate")
        if (!isUpdate)
            GoogleAnalytics.instance.sendEvent(UninstallEvent(version = pluginDescriptor.version))
    }

}