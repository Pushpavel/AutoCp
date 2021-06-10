package ui.poplist

import com.intellij.ui.OnePixelSplitter
import javax.swing.event.ListSelectionListener

class PopListImpl<T>(
    private val adapter: PopList<T>,
    private val popModel: PopListModel<T>,
    vertical: Boolean,
    proportion: Float
) : OnePixelSplitter(vertical, proportion) {

    private var itemViewCorrespondingIndex = -1

    private val selectionListener = ListSelectionListener {

        val index = popModel.selectionModel.minSelectionIndex
        when (index) {
            itemViewCorrespondingIndex -> return@ListSelectionListener
            -1 -> detachItemView()
            else -> {
                if (itemViewCorrespondingIndex == -1)
                    attachItemView()
                adapter.itemView.updateView(popModel.listModel.getElementAt(index))
            }
        }
        itemViewCorrespondingIndex = index
    }

    private fun attachItemView(): Any = adapter.itemContainer.add(adapter.itemView.component)
    private fun detachItemView() {
        adapter.itemContainer.remove(adapter.itemView.component)
        adapter.itemContainer.updateUI()
    }


    init {
        firstComponent = adapter.listContainer
        secondComponent = adapter.itemContainer
        adapter.listComponent.apply {
            selectionModel = popModel.selectionModel
            model = popModel.listModel
        }
        popModel.selectionModel.addListSelectionListener(selectionListener)
    }

}