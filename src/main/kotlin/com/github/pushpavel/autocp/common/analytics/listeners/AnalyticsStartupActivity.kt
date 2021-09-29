package com.github.pushpavel.autocp.common.analytics.listeners

import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.UpdateEvent
import com.github.pushpavel.autocp.common.helpers.isUpdating
import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class AnalyticsStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (PropertiesComponent.getInstance().isUpdating) {
            println("AutoCp Plugin Updated.")
            PluginManagerCore.getPlugin(PluginId.getId(R.keys.pluginId))?.version?.let { version ->
                GoogleAnalytics.instance.sendEvent(UpdateEvent(version = version))
                PropertiesComponent.getInstance().isUpdating = false
            }
        }
    }
}