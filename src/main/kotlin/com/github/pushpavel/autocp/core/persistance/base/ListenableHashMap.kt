package com.github.pushpavel.autocp.core.persistance.base

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer

typealias Handler<K, V> = (ListenableHashMap.Event<K, V>) -> Unit

open class ListenableHashMap<K, V> : ProtectedHashMap<K, V>() {

    sealed interface Event<K, V> {
        val key: K
        val value: V?

        data class Put<K, V>(override val key: K, override val value: V) : Event<K, V>
        data class Remove<K, V>(override val key: K, override val value: V) : Event<K, V>
        data class Replace<K, V>(override val key: K, override val value: V, val oldValue: V) : Event<K, V>
        data class Replay<K, V>(override val key: K, override val value: V?) : Event<K, V>
    }

    private val putEventListeners = mutableSetOf<Handler<K, V>>()
    private val removeEventListeners = mutableSetOf<Handler<K, V>>()
    private val replaceEventListeners = mutableSetOf<Handler<K, V>>()
    private val putListeners = mutableMapOf<K, MutableList<Handler<K, V>>>()
    private val removeListeners = mutableMapOf<K, MutableList<Handler<K, V>>>()
    private val replaceListeners = mutableMapOf<K, MutableList<Handler<K, V>>>()

    override fun put(key: K, value: V) {
        super.put(key, value)
        putEventListeners.forEach { it(Event.Put(key, value)) }
        putListeners[key]?.onEach { it(Event.Put(key, value)) }
    }

    override fun remove(key: K): V {
        val oldValue = super.remove(key)
        removeEventListeners.forEach { it(Event.Remove(key, oldValue)) }
        removeListeners[key]?.onEach { it(Event.Remove(key, oldValue)) }
        return oldValue
    }

    override fun replace(key: K, value: V): V {
        val oldValue = super.replace(key, value)
        replaceEventListeners.forEach { it(Event.Replace(key, value, oldValue)) }
        replaceListeners[key]?.onEach { it(Event.Replace(key, value, oldValue)) }
        return oldValue
    }

    /**
     * Adds a listener that will be called when a new value is put into the map.
     */
    fun onPut(listener: Handler<K, V>, parentDisposable: Disposable) {
        putEventListeners.add(listener)
        Disposer.register(parentDisposable) { putEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is removed from the map.
     */
    fun onRemove(listener: Handler<K, V>, parentDisposable: Disposable) {
        removeEventListeners.add(listener)
        Disposer.register(parentDisposable) { removeEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is replaced in the map.
     */
    fun onReplace(listener: Handler<K, V>, parentDisposable: Disposable) {
        replaceEventListeners.add(listener)
        Disposer.register(parentDisposable) { replaceEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when value of [key] is changed.
     */
    fun onKey(
        key: K,
        listener: Handler<K, V>,
        parentDisposable: Disposable,
        replace: Boolean = true,
        put: Boolean = true,
        remove: Boolean = true,
        replay: Boolean = false,
    ) {
        if (replay)
            listener(Event.Replay(key, get(key)))

        if (put) putListeners.getOrPut(key) { mutableListOf() }.add(listener)
        if (replace) replaceListeners.getOrPut(key) { mutableListOf() }.add(listener)
        if (remove) removeListeners.getOrPut(key) { mutableListOf() }.add(listener)
        Disposer.register(parentDisposable) {
            putListeners[key]?.remove(listener)
            replaceListeners[key]?.remove(listener)
            removeListeners[key]?.remove(listener)
        }
    }
}