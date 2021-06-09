package ui

import javax.swing.ListModel
import javax.swing.ListSelectionModel

interface PopListAdapter<T> {

    fun submitList(list: List<T>): Boolean
    fun addItem(index: Int, item: T): Boolean
    fun removeItem(index: Int): T?
    fun updateItem(index: Int, item: T): Boolean
    fun selectItem(index: Int): Boolean

    fun addListEventsListener(listener: Listener<T>)
    fun removeListEventsListener(listener: Listener<T>)

    fun getItems(): List<T>
    fun getSize(): Int

    fun getNameListModel(): ListModel<String>
    fun getSelectionModel(): ListSelectionModel
    fun getSelectedIndex(): Int

    interface Listener<T> {
        fun add(index: Int, item: T)
        fun remove(index: Int, item: T)
        fun update(index: Int, item: T)
        fun select(selection: Int, item: T?)
    }
}