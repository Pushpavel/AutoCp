package com.github.pushpavel.autocp.core.persistance.base

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer

typealias Handler<V> = ListenableHashMap.Event.(V) -> Unit

open class ListenableHashMap<K, V> : ProtectedHashMap<K, V>() {

    enum class Event { PUT, REMOVE, REPLACE }

    private val putEventListeners = mutableSetOf<Handler<V>>()
    private val removeEventListeners = mutableSetOf<Handler<V>>()
    private val replaceEventListeners = mutableSetOf<Handler<V>>()
    private val putListeners = mutableMapOf<K, MutableList<Handler<V>>>()
    private val removeListeners = mutableMapOf<K, MutableList<Handler<V>>>()
    private val replaceListeners = mutableMapOf<K, MutableList<Handler<V>>>()

    override fun put(key: K, value: V) {
        super.put(key, value)
        putEventListeners.forEach { Event.PUT.it(value) }
        putListeners[key]?.onEach { Event.PUT.it(value) }
    }

    override fun remove(key: K): V {
        val oldValue = super.remove(key)
        removeEventListeners.forEach { Event.REMOVE.it(oldValue) }
        removeListeners[key]?.onEach { Event.REMOVE.it(oldValue) }
        return oldValue
    }

    override fun replace(key: K, value: V): V {
        val oldValue = super.replace(key, value)
        replaceEventListeners.forEach { Event.REPLACE.it(oldValue) }
        replaceListeners[key]?.onEach { Event.REPLACE.it(oldValue) }
        return oldValue
    }

    /**
     * Adds a listener that will be called when a new value is put into the map.
     */
    fun onPut(listener: Handler<V>, parentDisposable: Disposable) {
        putEventListeners.add(listener)
        Disposer.register(parentDisposable) { putEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is removed from the map.
     */
    fun onRemove(listener: Handler<V>, parentDisposable: Disposable) {
        removeEventListeners.add(listener)
        Disposer.register(parentDisposable) { removeEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is replaced in the map.
     */
    fun onReplace(listener: Handler<V>, parentDisposable: Disposable) {
        replaceEventListeners.add(listener)
        Disposer.register(parentDisposable) { replaceEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when value of [key] is changed.
     */
    fun onKey(
        key: K,
        listener: Handler<V>,
        parentDisposable: Disposable,
        replace: Boolean = true,
        put: Boolean = true,
        remove: Boolean = true
    ) {
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