package com.github.pushpavel.autocp.core.persistance.base

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer

open class ListenableHashMap<K, V> : ProtectedHashMap<K, V>() {
    private val putEventListeners = mutableSetOf<(V) -> Unit>()
    private val removeEventListeners = mutableSetOf<(V) -> Unit>()
    private val replaceEventListeners = mutableSetOf<(V) -> Unit>()

    override fun put(key: K, value: V) {
        super.put(key, value)
        putEventListeners.forEach { it(value) }
    }

    override fun remove(key: K): V {
        val oldValue = super.remove(key)
        removeEventListeners.forEach { it(oldValue) }
        return oldValue
    }

    override fun replace(key: K, value: V): V {
        val oldValue = super.replace(key, value)
        replaceEventListeners.forEach { it(oldValue) }
        return oldValue
    }

    /**
     * Adds a listener that will be called when a new value is put into the map.
     */
    fun onPut(listener: (V) -> Unit, parentDisposable: Disposable) {
        putEventListeners.add(listener)
        Disposer.register(parentDisposable) { putEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is removed from the map.
     */
    fun onRemove(listener: (V) -> Unit, parentDisposable: Disposable) {
        removeEventListeners.add(listener)
        Disposer.register(parentDisposable) { removeEventListeners.remove(listener) }
    }

    /**
     * Adds a listener that will be called when a value is replaced in the map.
     */
    fun onReplace(listener: (V) -> Unit, parentDisposable: Disposable) {
        replaceEventListeners.add(listener)
        Disposer.register(parentDisposable) { replaceEventListeners.remove(listener) }
    }
}