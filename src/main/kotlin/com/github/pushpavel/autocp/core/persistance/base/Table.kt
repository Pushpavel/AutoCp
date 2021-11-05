package com.github.pushpavel.autocp.core.persistance.base

/**
 * Manages a collection of entries in memory
 */
abstract class Table<K, V> : ListenableHashMap<K, V>() {

    /**
     * Returns a unique key from the given value
     */
    abstract fun getKey(value: V): K

    /**
     * Puts value into the table
     */
    fun put(value: V) {
        val key = getKey(value)
        put(key, value)
    }

    /**
     * Removes the value from the table
     */
    fun remove(value: V) {
        val key = getKey(value)
        remove(key)
    }

    /**
     * Replaces the value in the table
     */
    fun replace(value: V) {
        val key = getKey(value)
        replace(key, value)
    }
}