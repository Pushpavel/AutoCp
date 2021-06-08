package ui

import javax.swing.JComponent

interface ItemAdapter<T> {
    fun createItemView(item: T): JComponent
    fun updateItemView(item: T)

    fun getItemName(item: T): String
    fun isModified(item1: T, item2: T): Boolean
}