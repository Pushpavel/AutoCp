package com.github.pushpavel.autocp.database

import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class AutoCpExternalReloader : BulkFileListener {

    override fun after(events: List<VFileEvent>) {
        val autocpEvents = events.asSequence()
            .filterIsInstance<VFileContentChangeEvent>()
            .filter { it.file.name == ".autocp" }
            .toList()
        if (autocpEvents.isEmpty()) return

        val openProjects = ProjectManager.getInstanceIfCreated()?.openProjects ?: return
        for (event in autocpEvents) {
            val parentPath = event.file.parent?.path ?: continue
            for (project in openProjects) {
                if (project.isDefault) continue
                if (project.basePath == parentPath) {
                    project.service<AutoCpStorage>().reloadFromDisk()
                }
            }
        }
    }
}
