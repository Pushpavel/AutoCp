package com.github.pushpavel.autocp.core.persistance.base

import kotlinx.coroutines.flow.MutableSharedFlow

open class MapWithEventFlow<K, V> : ProtectedMap<K, V>() {
    enum class EventType { ADD, REMOVE, UPDATE }
    data class Event<K, V>(val oldMap: Map<K, V>, val map: Map<K, V>, val keys: Set<K>) {
        val type
            get() = when {
                oldMap.size > map.size -> EventType.REMOVE
                oldMap.size < map.size -> EventType.ADD
                else -> EventType.UPDATE
            }
    }

    protected val events = MutableSharedFlow<Event<K, V>>()

    override fun put(key: K, value: V): V? {
        val oldMap = map.toMap()
        val oldValue = super.put(key, value)
        events.tryEmit(Event(oldMap, this, setOf(key)))
        return oldValue
    }

    override fun clear() {
        if (map.isEmpty()) return
        val oldMap = map.toMap()
        super.clear()
        events.tryEmit(Event(oldMap, this, oldMap.keys))
    }

    override fun remove(key: K): V? {
        val oldMap = map.toMap()
        val oldValue = super.remove(key)
        if (oldValue != null)
            events.tryEmit(Event(oldMap, this, setOf(key)))
        return oldValue
    }
}