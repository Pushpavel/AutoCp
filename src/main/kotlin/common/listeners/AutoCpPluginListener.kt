package common.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.project.ProjectManager
import common.res.R
import gather.ProblemGatheringService

class AutoCpPluginListener : DynamicPluginListener {
    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString != R.keys.pluginId) return
        ProjectManager.getInstance().openProjects.forEach {
            val service = it.serviceIfCreated<ProblemGatheringService>()
            service?.stopService()
        }
    }
}