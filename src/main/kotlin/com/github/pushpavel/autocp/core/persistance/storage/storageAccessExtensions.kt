package com.github.pushpavel.autocp.core.persistance.storage

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * Loads [StorageManager] on project startup.
 */
class StorageManagerLoader : StartupActivity {
    override fun runActivity(project: Project) {
        project.service<StorageManager>().load()
    }

}

fun Project.storageManager() = service<StorageManager>().also {
    if (!it.isLoaded)
        throw IllegalStateException("Storage manager is not yet loaded, but is requested")
}

inline fun <reified T : Storable> Project.storable(): T {
    val storageManager = storageManager()
    val type = T::class.java

    for (storable in storageManager.storables.values) {
        if (storable::class.java == type)
            return storable as T
    }

    throw IllegalStateException("Storable $type is not registered")
}