package ui


abstract class AbstractPopListAdapter<T> : PopListAdapter<T> {
    private val listeners = ArrayList<PopListAdapter.Listener<T>>()

    protected abstract fun submitListImpl(list: List<T>): Boolean
    protected abstract fun addItemImpl(index: Int, item: T): Boolean
    protected abstract fun removeItemImpl(index: Int): T?
    protected abstract fun updateItemImpl(index: Int, item: T): Boolean
    protected abstract fun selectItemImpl(index: Int): Boolean

    override fun submitList(list: List<T>): Boolean {
        return submitListImpl(list)
    }

    override fun addItem(index: Int, item: T): Boolean {
        return addItemImpl(index, item).also {
            if (it) fireAddEvent(index, item)
        }
    }

    override fun removeItem(index: Int): T? {
        return removeItemImpl(index).also {
            if (it != null) fireRemoveEvent(index, it)
        }
    }

    override fun updateItem(index: Int, item: T): Boolean {
        return updateItemImpl(index, item).also {
            if (it) fireUpdateEvent(index, item)
        }
    }

    override fun selectItem(index: Int): Boolean {
        return selectItemImpl(index).also {
            if (it)
                fireSelectEvent(index, getItems()[index])
        }
    }

    private fun fireAddEvent(index: Int, item: T) = listeners.forEach { it.add(index, item) }

    private fun fireRemoveEvent(index: Int, item: T) = listeners.forEach { it.remove(index, item) }
    private fun fireUpdateEvent(index: Int, item: T) = listeners.forEach { it.update(index, item) }
    private fun fireSelectEvent(selection: Int, item: T?) = listeners.forEach { it.select(selection, item) }


    override fun addListEventsListener(listener: PopListAdapter.Listener<T>) {
        listeners.add(listener)
    }

    override fun removeListEventsListener(listener: PopListAdapter.Listener<T>) {
        listeners.remove(listener)
    }
}