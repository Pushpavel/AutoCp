package ui

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel

abstract class BasePopListAdapter<T> : AbstractPopListAdapter<T>() {
    private val namesListModel = CollectionListModel<String>()
    private val selectionModel = SingleSelectionModel()

    private val internalList = ArrayList<T>()

    abstract fun isModified(item1: T, item2: T): Boolean
    abstract fun getItemName(item: T): String


    override fun submitListImpl(list: List<T>): Boolean {
        var didUpdate = false
        var i = 0
        val listIter = list.iterator()

        while (i < list.size && i < internalList.size) {
            if (updateItem(i, listIter.next()))
                didUpdate = true
            i++
        }

        listIter.forEachRemaining {
            if (addItem(i, it)) didUpdate = true
            i++
        }

        while (i < internalList.size) {
            if (removeItem(i) != null)
                didUpdate = true
            i++
        }
        return didUpdate
    }

    override fun addItemImpl(index: Int, item: T): Boolean {
        return runCatching {
            internalList.add(index, item)
            namesListModel.add(index, getItemName(item))
            true
        }.getOrDefault(false)
    }

    override fun removeItemImpl(index: Int): T? {
        return runCatching {
            val removedValue = internalList.removeAt(index)
            namesListModel.remove(index)
            removedValue
        }.getOrNull()
    }

    override fun updateItemImpl(index: Int, item: T): Boolean {
        return runCatching {
            val modified = isModified(internalList[index], item)
            if (modified) {
                internalList[index] = item
                namesListModel.setElementAt(getItemName(item), index)
            }
            return modified
        }.getOrDefault(false)
    }

    override fun selectItemImpl(index: Int): Boolean {
        if (index >= getSize() || index < -1)
            return false
        selectionModel.setSelectionInterval(index, index)
        return true
    }

    override fun getItems() = internalList

    override fun getSize() = internalList.size

    override fun getNameListModel() = namesListModel

    override fun getSelectionModel() = selectionModel

    override fun getSelectedIndex() = getSelectionModel().minSelectionIndex


//
//    fun submitList(list: List<T>) {
////        val adapter = getItemAdapter()
////        val listIter = list.iterator()
////        val internalListIter = internalList.iterator()
////
////        while (listIter.hasNext() && internalListIter.hasNext()) {
////            val item = listIter.next()
////            if (adapter.isModified(internalListIter.next(), item)) {
////                updateItem()
////            }
////        }
//    }
//
//    fun addItem(index: Int, item: T) {
////        val adapter = getItemAdapter()
////        val itemName = adapter.getItemName(item)
////        namesListModel.add(index, itemName)
//    }
//
//    fun removeItem(index: Int) {
//        namesListModel.remove(index)
//    }
//
//    fun updateItem(index: Int, item: T) {
//
//    }
//
//    fun getSize() = internalList.size
//
//
//    abstract fun isModified(item1: T, item2: T): Boolean
//
//    abstract fun getItemName(item: T): String
}