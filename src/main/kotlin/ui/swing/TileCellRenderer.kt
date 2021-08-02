package ui.swing

import com.intellij.ui.SimpleListCellRenderer
import java.awt.Component
import javax.swing.BorderFactory
import javax.swing.JList


class TileCellRenderer<T>(
    private val padding: Int = 4,
    private val cellBuilder: TileCellRenderer<T>.(T) -> Unit
) : SimpleListCellRenderer<T>() {

    override fun getListCellRendererComponent(
        list: JList<out T>?,
        value: T,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus).apply {
            val paddingBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
            border = BorderFactory.createCompoundBorder(border, paddingBorder)
        }
    }

    override fun customize(list: JList<out T>, value: T, index: Int, selected: Boolean, hasFocus: Boolean) {
        cellBuilder(value)
    }
}