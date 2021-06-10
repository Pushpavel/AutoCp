package ui.poplist

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel

class PopListModel<T> {
    val listModel = CollectionListModel<T>()
    val selectionModel = SingleSelectionModel()

    private val selectionListeners = ArrayList<SelectionListener>()

    // helper for subscribing and to set selection index

    private var lastSelectedIndex = selectionModel.minSelectionIndex

    init {
        // notify selection Listeners
        selectionModel.addListSelectionListener {
            if (lastSelectedIndex != selectionModel.minSelectionIndex) {
                selectionListeners.forEach { listener ->
                    listener.onSelect(selectionModel.minSelectionIndex)
                }
            }
            lastSelectedIndex = selectionModel.minSelectionIndex
        }
    }

    fun addSelectionListener(selectionListener: SelectionListener) {
        selectionListeners.add(selectionListener)
    }

    fun removeSelectionListener(selectionListener: SelectionListener) {
        selectionListeners.remove(selectionListener)
    }

    fun interface SelectionListener {
        fun onSelect(index: Int)
    }
}