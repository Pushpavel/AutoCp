package settings.base

import javax.swing.ComboBoxModel
import javax.swing.ListModel
import javax.swing.event.ListDataListener

class ComboBoxModelDelegate<T>(private val sourceListModel: ListModel<T>) : ComboBoxModel<T> {

    private var selection: T? = null;

    @Suppress("UNCHECKED_CAST")
    override fun setSelectedItem(item: Any?) {
        this.selection = item as T?
    }

    override fun getSelectedItem(): T? {
        return this.selection
    }

    // Delegates
    override fun getSize() = sourceListModel.size
    override fun getElementAt(p0: Int): T = sourceListModel.getElementAt(p0)
    override fun addListDataListener(p0: ListDataListener?) = sourceListModel.addListDataListener(p0)
    override fun removeListDataListener(p0: ListDataListener?) = sourceListModel.removeListDataListener(p0)
}