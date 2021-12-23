package com.github.pushpavel.autocp.core.persistance.storables.base

abstract class ProtectedMap<K, V> : Map<K, V> {
    protected val map = mutableMapOf<K, V>()

    override val entries: Set<Map.Entry<K, V>> get() = map.entries
    override val keys: Set<K> get() = map.keys
    override val size: Int get() = map.size
    override val values: Collection<V> get() = map.values

    override fun containsKey(key: K): Boolean = map.containsKey(key)
    override fun containsValue(value: V): Boolean = map.containsValue(value)
    override fun get(key: K): V? = map[key]
    override fun isEmpty(): Boolean = map.isEmpty()

    // mutable methods
    protected open fun clear() = map.clear()

    protected open fun put(key: K, value: V): V? = map.put(key, value)

    protected open fun putAll(from: Map<out K, V>) = map.putAll(from)

    protected open fun remove(key: K): V? = map.remove(key)
}