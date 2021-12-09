package com.github.pushpavel.autocp.core.persistance.storage

import com.google.gson.JsonObject
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import org.kodein.memory.util.forEachCatch

/**
 * System to gracefully
 * - support multiple storage locations. see [StorageChannel]
 * - solve incomplete data issues due to schema changes. see [StorableProcessor]
 */
@Service
class StorageManager(private val project: Project) {

    private val log = Logger.getInstance(StorageManager::class.java)

    private val data = mutableMapOf<String, JsonObject>()
    private val channels = listOf<StorageChannel>()
    private val processors = listOf<StorableProcessor>()
    private val storables = listOf<Storable>()

    fun load() {
        data.clear()

        // load data from channels
        channels.forEach {
            val channelData = it.load(project)
            for ((key, value) in channelData) {
                // apply channel data only if it contains more data than any other channel
                if (key !in data || data.getValue(key).size() <= value.size())
                    data[key] = value
            }
        }

        // process data from processors
        processors.forEachCatch {
            it.process(project, data)
        }?.also { log.error(it) }

        // apply data to storables
        storables.forEachCatch {
            if (it.id in data)
                it.load(data.getValue(it.id))
        }?.also { log.error(it) }
    }

    fun save() {
        data.clear()

        // get data from storables
        storables.forEachCatch {
            data[it.id] = it.save()
        }?.also { log.error(it) }

        // save data to channels
        channels.forEachCatch {
            it.save(project, data)
        }?.also { log.error(it) }
    }
}