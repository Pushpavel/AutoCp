package ui

import com.intellij.ui.JBSplitter
import java.awt.Component
import javax.swing.JComponent
import javax.swing.ListModel
import javax.swing.ListSelectionModel
import javax.swing.SingleSelectionModel

class PopListView<T>(
    isVertical: Boolean, proportion: Float,
    private val adapter: PopListAdapter<T>,
    private val viewAdapter: ViewAdapter<T>
) : JBSplitter(isVertical, proportion) {


    init {
        firstComponent = viewAdapter.createNameListView(adapter.getNameListModel(), adapter.getSelectionModel())
        secondComponent = viewAdapter.createItemHolderPanel()

        adapter.addListEventsListener(object : BasePopListListener<T>() {
            override fun update(index: Int, item: T) {
                if (adapter.getSelectedIndex() == index)
                    viewAdapter.updateItemView(item)
            }

            override fun select(selection: Int, item: T?) {
                if (item != null) {
                    showItemView()
                    update(selection, item)
                } else
                    hideItemView()
            }
        })

    }


    private fun showItemView() {
        if (secondComponent.componentCount == 0)
            secondComponent.add(viewAdapter.itemView)
    }

    private fun hideItemView() {
        if (secondComponent.componentCount != 0)
            secondComponent.removeAll()
    }

    interface ViewAdapter<T> {
        val itemView: Component

        fun createNameListView(
            listModel: ListModel<String>,
            selectionModel: ListSelectionModel
        ): JComponent

        fun createItemHolderPanel(): JComponent

        fun updateItemView(item: T)
    }

}