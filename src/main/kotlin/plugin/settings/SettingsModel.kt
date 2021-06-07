package plugin.settings

import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SingleSelectionModel

class SettingsModel : CollectionComboBoxModel<SolutionLanguage>() {

    val languagePanelModel = LanguagePanelUI.Model()
    val sideListSelectionModel = SingleSelectionModel()

    init {
        sideListSelectionModel.addListSelectionListener {
            // saving last selected Item to list
            applyLanguagePanelModelToListModel()
            // applying current selected Item from list
            applyListModelToLanguagePanelModel()
        }
    }

    fun applyLanguagePanelModelToListModel() {
        // return if languagePanel didn't already correspond to any item in the list
        val item = languagePanelModel.getCorrespondingItem() ?: return
        val index = getElementIndex(item)

        // applying the new item value to the item
        if (index != -1) {
            val value = languagePanelModel.createItemValue()
            setElementAt(value, index)
            languagePanelModel.setCorrespondingItem(value)
        }
    }

    fun applyListModelToLanguagePanelModel() {
        val selectedIndex = sideListSelectionModel.minSelectionIndex
        val selectedItem = selectedIndex.takeIf { it != -1 }?.let { getElementAt(it) }
        languagePanelModel.applyItem(selectedItem)
    }
}