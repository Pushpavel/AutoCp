package ui

import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

open class StringCellRenderer<out T>(private val textGetter: (T) -> String) : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        @Suppress("UNCHECKED_CAST")
        val data = value as T?
        val text = if (data != null) textGetter(data) else null
        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus)
    }
}