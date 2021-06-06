package plugin.ui

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.components.JBList
import plugin.settings.SolutionLanguage

class SolutionLanguagesListModel(private val list: JBList<SolutionLanguage>) : CollectionListModel<SolutionLanguage>() {

    init {
        list.selectionModel = SingleSelectionModel()
    }

    fun setSelectedIndex(index: Int) {
        if (size <= index || index < 0)
            throw IndexOutOfBoundsException("SolutionLanguagesListModel: index=$index size=$size")
        list.selectedIndex = index
    }

    fun getSelectedIndex() = list.selectedIndex

    fun getSelection() = if (list.selectedIndex != -1) getElementAt(list.selectedIndex) else null
}