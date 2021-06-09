package tests.ui

import ui.BasePopListAdapter
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class BasePopListAdapterImpl : BasePopListAdapter<ItemStub>() {

    override fun isModified(item1: ItemStub, item2: ItemStub) = item1 != item2

    override fun getItemName(item: ItemStub) = item.name
}

class NameListListenerImpl : ListDataListener {

    @Suppress("UNUSED_PARAMETER")
    fun add(index: Int) {}

    @Suppress("UNUSED_PARAMETER")
    fun remove(index: Int) {}

    @Suppress("UNUSED_PARAMETER")
    fun update(index: Int) {}

    override fun intervalAdded(p0: ListDataEvent?) {
        p0 ?: return
        for (i in p0.index0..p0.index1)
            add(i)
    }

    override fun intervalRemoved(p0: ListDataEvent?) {
        p0 ?: return
        for (i in p0.index0..p0.index1)
            remove(i)
    }

    override fun contentsChanged(p0: ListDataEvent?) {
        p0 ?: return
        for (i in p0.index0..p0.index1)
            update(i)
    }

}