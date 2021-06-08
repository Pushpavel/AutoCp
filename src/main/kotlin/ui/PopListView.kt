package ui

import com.intellij.ui.JBSplitter

class PopListView<T>(
    proportion: Float,
    adapter: AbstractPopListAdapter<T>
) : JBSplitter(proportion) {

    init {
        firstComponent = adapter.createNameListView(adapter.namesListModel, adapter.selectionModel)
        secondComponent = adapter.createItemHolderPanel()
    }


}