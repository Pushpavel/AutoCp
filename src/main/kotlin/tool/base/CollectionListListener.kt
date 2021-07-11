package tool.base

import com.intellij.ui.CollectionListModel
import javax.swing.ListModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

/**
 * Helper Class to simplify [ListDataListener] by redirecting other events to onChange event
 */
@Suppress("UNCHECKED_CAST")
abstract class CollectionListListener<T> : ListDataListener {

    abstract fun onChange(items: List<T>)

    override fun intervalAdded(event: ListDataEvent) {
        onChange((event.source as CollectionListModel<T>).items)
    }

    override fun intervalRemoved(event: ListDataEvent) {
        onChange((event.source as CollectionListModel<T>).items)
    }

    override fun contentsChanged(event: ListDataEvent) {
        onChange((event.source as CollectionListModel<T>).items)
    }
}