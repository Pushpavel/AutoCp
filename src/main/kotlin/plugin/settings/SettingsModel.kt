package plugin.settings

import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SingleSelectionModel
import kotlin.math.max

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

    fun duplicateOrAddItem() {
        val selectedIndex = sideListSelectionModel.minSelectionIndex.takeIf { it != -1 }
        val newItem = selectedIndex
            ?.let { getElementAt(it).copy() }
            ?: AutoCpSettings.getSolutionLanguageTemplate()

        newItem.name = nextUniqueName(newItem.name)

        val newItemIndex = selectedIndex?.let { it + 1 } ?: size

        add(newItemIndex, newItem)
        sideListSelectionModel.setSelectionInterval(newItemIndex, newItemIndex)

        // select preferred language if not set
        if (selected == null)
            selectedItem = newItem
    }

    private fun nextUniqueName(preferredName: String): String {
        if (isUniqueName(preferredName))
            return preferredName

        val prefix =
            AutoCpSettings.DUPLICATE_NAME_REGEX.matchEntire(preferredName)?.groupValues?.get(1) ?: preferredName

        // find appropriate number for suffixing
        var maxNumberSuffix = 0
        items.forEach {
            val matches = AutoCpSettings.DUPLICATE_NAME_REGEX.matchEntire(it.name)?.groupValues
            if (matches != null && matches[1] == prefix)
                maxNumberSuffix = max(maxNumberSuffix, matches[2].toInt())
        }

        maxNumberSuffix++

        return "${prefix}_$maxNumberSuffix"
    }

    private fun isUniqueName(name: String) = items.all { it.name != name }

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