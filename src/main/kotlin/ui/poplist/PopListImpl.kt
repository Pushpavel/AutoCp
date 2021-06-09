package ui.poplist

import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.SingleSelectionModel
import javax.swing.event.ListSelectionListener

class PopListImpl<T>(private val adapter: PopList<T>, vertical: Boolean, proportion: Float) :
    OnePixelSplitter(vertical, proportion) {

    private var selectModel = SingleSelectionModel()
    private var itemViewCorrespondingIndex = -1

    private val selectionListener = ListSelectionListener {

        val index = selectModel.minSelectionIndex
        when (index) {
            itemViewCorrespondingIndex -> return@ListSelectionListener
            -1 -> detachItemView()
            else -> {
                if (itemViewCorrespondingIndex == -1)
                    attachItemView()
                adapter.itemView.updateView(adapter.listModel.getElementAt(index))
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
            selectionModel = selectModel
            model = adapter.listModel
        }
        selectModel.addListSelectionListener(selectionListener)
    }

}