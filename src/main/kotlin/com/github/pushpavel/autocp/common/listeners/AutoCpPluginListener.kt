package com.github.pushpavel.autocp.common.listeners

import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.InstallEvent
import com.github.pushpavel.autocp.common.analytics.events.UninstallEvent
import com.github.pushpavel.autocp.common.analytics.events.UpdateEvent
import com.github.pushpavel.autocp.common.helpers.analyticsClientIdOrNull
import com.github.pushpavel.autocp.common.helpers.unsetAnalyticsClientId
import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.gather.ProblemGatheringService
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.project.ProjectManager

class AutoCpPluginListener : DynamicPluginListener {
    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        ProjectManager.getInstance().openProjects.forEach {
            val service = it.serviceIfCreated<ProblemGatheringService>()
            service?.stopService()
        }

        if (!isUpdate) {
            PropertiesComponent.getInstance().unsetAnalyticsClientId()
            GoogleAnalytics.instance.sendEvent(UninstallEvent(version = pluginDescriptor.version))
        }
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        val analytics = GoogleAnalytics.instance
        analytics.sendEvent(
            if (PropertiesComponent.getInstance().analyticsClientIdOrNull() == null)
                InstallEvent(version = pluginDescriptor.version)
            else
                UpdateEvent(version = pluginDescriptor.version)
        )
    }
}