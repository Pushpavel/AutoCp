package com.github.pushpavel.autocp.core.persistance.storage

import com.github.pushpavel.autocp.core.persistance.solutions.Solutions
import com.github.pushpavel.autocp.core.persistance.storage.channels.PropertiesComponentChannel
import com.github.pushpavel.autocp.core.persistance.testcases.Testcases
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


    private val channels = listOf<StorageChannel>(
        PropertiesComponentChannel()
    )
    private val processors = listOf<StorableProcessor>()

    val storables = mapOf<String, Storable>(
        "solutions" to Solutions(),
        "testcases" to Testcases()
    )

    var isLoaded = false
        private set

    private val data = mutableMapOf<String, JsonObject>()

    fun load() {
        if (isLoaded) return
        data.clear()

        val keys = storables.keys.toList()

        // load data from channels
        channels.forEach {
            val channelData = it.load(project, keys)
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
        storables.entries.forEachCatch { (key, value) ->
            if (key in data)
                value.load(data.getValue(key))
        }?.also { log.error(it) }

        isLoaded = true
    }

    fun save() {
        data.clear()

        // get data from storables
        storables.entries.forEachCatch { (key, value) ->
            data[key] = value.save()
        }?.also { log.error(it) }

        // save data to channels
        channels.forEachCatch {
            it.save(project, data)
        }?.also { log.error(it) }
    }
}