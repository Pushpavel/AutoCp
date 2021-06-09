package ui.poplist

import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListModel

abstract class PopList<T>(isVertical: Boolean, proportion: Float, val listModel: ListModel<T>) {


    abstract val listComponent: JList<T>
    abstract val listContainer: JComponent

    abstract val itemContainer: JComponent
    abstract val itemView: ItemView<T>

    val component by lazy { PopListImpl(this, isVertical, proportion) }

    interface ItemView<T> {
        val component: JComponent
        fun updateView(item: T)
    }
}