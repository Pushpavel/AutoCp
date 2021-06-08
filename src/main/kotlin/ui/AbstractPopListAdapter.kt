package ui

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel
import javax.swing.JComponent
import javax.swing.ListSelectionModel

abstract class AbstractPopListAdapter<T> {
    val namesListModel = CollectionListModel<String>()
    val selectionModel = SingleSelectionModel()

    val internalList = ArrayList<T>()

    fun submitList(list: Iterable<T>) {

    }

    fun addItem(index: Int, item: T) {
        val adapter = getItemAdapter()
        val itemName = adapter.getItemName(item)
        namesListModel.add(index, itemName)
    }

    fun removeItem(index: Int) {
        namesListModel.remove(index)
    }

    abstract fun createNameListView(
        listModel: CollectionListModel<String>,
        selectionModel: ListSelectionModel
    ): JComponent

    abstract fun createItemHolderPanel(): JComponent

    abstract fun getItemAdapter(): ItemAdapter<T>
}