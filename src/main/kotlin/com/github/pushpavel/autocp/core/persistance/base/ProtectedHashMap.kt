package com.github.pushpavel.autocp.core.persistance.base

/**
 * HashMap without exposing its internal state.
 */
open class ProtectedHashMap<K, V> : Map<K, V> {
    protected val map = HashMap<K, V>()
    override val size: Int get() = map.size
    override fun containsKey(key: K): Boolean = map.containsKey(key)
    override fun containsValue(value: V): Boolean = map.containsValue(value)
    override fun get(key: K): V? = map[key]
    override fun isEmpty(): Boolean = map.isEmpty()

    override val entries: Set<Map.Entry<K, V>> get() = map.entries
    override val keys: Set<K> get() = map.keys
    override val values: Collection<V> get() = map.values

    open fun put(key: K, value: V) {
        if (map.containsKey(key))
            throw IllegalArgumentException("Key $key already exists")

        map[key] = value
    }

    open fun remove(key: K): V {
        if (!map.containsKey(key))
            throw IllegalArgumentException("Key $key does not exist")

        return map.remove(key)!!
    }

    open fun replace(key: K, value: V): V {
        if (!map.containsKey(key))
            throw IllegalArgumentException("Key $key does not exist")

        val oldValue = remove(key)
        put(key, value)
        return oldValue
    }
}