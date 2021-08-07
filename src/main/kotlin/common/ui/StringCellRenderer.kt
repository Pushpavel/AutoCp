package common.ui

import java.awt.Component
import javax.swing.BorderFactory
import javax.swing.DefaultListCellRenderer
import javax.swing.Icon
import javax.swing.JList

/**
 * CellRenderer that maps model classes to presentable names with icons in ListViews
 * through @param cellGetter
 */
open class StringCellRenderer<out T>(private val cellGetter: (T) -> Pair<String, Icon?>?) :
    DefaultListCellRenderer() {

    private val padding: Int = 4

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        @Suppress("UNCHECKED_CAST")
        val data = value as T?

        val cell = if (data != null) cellGetter(data) else null

        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus)
        // padding
        val paddingBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        border = BorderFactory.createCompoundBorder(border, paddingBorder)

        if (cell != null) {
            text = cell.first
            icon = cell.second
        } else {
            text = ""
            icon = null
        }
        return this
    }


}