package com.github.pushpavel.autocp.common.listeners

import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.InstallEvent
import com.github.pushpavel.autocp.common.analytics.events.UninstallEvent
import com.github.pushpavel.autocp.common.helpers.isUpdating
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.util.PropertiesComponent

class AutoCpPluginListener : DynamicPluginListener {

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        println("AutoCp Plugin Loaded isInstall:${!PropertiesComponent.getInstance().isUpdating}")
        val analytics = GoogleAnalytics.instance
        if (!PropertiesComponent.getInstance().isUpdating)
            analytics.sendEvent(InstallEvent(version = pluginDescriptor.version))
        // sending Update Event in AnalyticsStartupActivity as pluginUnload may fail and this method may not be called
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        println("AutoCp Plugin Unloading... isUpdate:$isUpdate")
        if (!isUpdate) {
            GoogleAnalytics.instance.sendEvent(UninstallEvent(version = pluginDescriptor.version))
        } else
            PropertiesComponent.getInstance().isUpdating = true
    }

}