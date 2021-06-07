package files.editor

import com.intellij.ui.CollectionListModel
import com.intellij.ui.SingleSelectionModel
import files.TestcaseSpec
import javax.swing.event.ListDataListener

class ProblemModel : CollectionListModel<TestcaseSpec>() {

    val sidePanelSelectionModel = SingleSelectionModel()
    val testcaseModel = TestcasePanel.Model()

    var problemDataListener: ((ProblemData) -> Unit)? = null
    val testcasesListeners = ArrayList<(List<TestcaseSpec>) -> Unit>()

    fun updateState(state: ProblemData) = problemDataListener?.let { it(state) }

    init {
        sidePanelSelectionModel.addListSelectionListener {
            // saving last selected Item to list
            applyTestcaseModelToListModel()
            // applying current selected Item from list
            applyListModelToTestcaseModel()

            testcasesListeners.forEach { listener -> listener(items) }
        }
    }


    fun applyTestcaseModelToListModel() {
        // return if testcaseModel didn't already correspond to any item in the list
        val item = testcaseModel.getCorrespondingItem() ?: return
        val index = getElementIndex(item)

        // applying the new item value to the item
        if (index != -1)
            testcaseModel.createItemValue()?.let {
                setElementAt(it, index)
                testcaseModel.setCorrespondingItem(item)
            }
    }

    fun applyListModelToTestcaseModel() {
        val selectedIndex = sidePanelSelectionModel.minSelectionIndex
        val selectedItem = selectedIndex.takeIf { it != -1 }?.let { getElementAt(it) }
        testcaseModel.applyItem(selectedItem)
    }

    data class ProblemData(
        var name: String = "",
        var group: String = ""
    )
}