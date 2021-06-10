package ui.poplist

import javax.swing.JComponent
import javax.swing.JList

abstract class PopList<T>(isVertical: Boolean, proportion: Float, val popModel: PopListModel<T>) {


    abstract val listComponent: JList<T>
    abstract val listContainer: JComponent

    abstract val itemContainer: JComponent
    abstract val itemView: ItemView<T>

    val component by lazy { PopListImpl(this, popModel, isVertical, proportion) }

    interface ItemView<T> {
        val component: JComponent
        fun updateView(item: T)
    }
}