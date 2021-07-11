package ui.poplist

import javax.swing.JComponent
import javax.swing.JList

/**
 * List along UI of the selected ListItem on the side
 */
abstract class PopList<T>(isVertical: Boolean, proportion: Float, private val popModel: PopListModel<T>) {


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