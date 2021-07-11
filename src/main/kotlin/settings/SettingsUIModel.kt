package settings

import settings.base.ComboBoxModelDelegate
import ui.poplist.PopListModel

/**
 * ViewModel for [SettingsUI]
 */
class SettingsUIModel {
    val popListModel = object : PopListModel<SolutionLanguage>() {
        override val itemNameRegex = Regex("^(.*)_([0-9]+)\$")

        override fun getItemName(item: SolutionLanguage) = item.name

        override fun buildItemName(name: String, suffix: String) = "${name}_${suffix}"

        override fun createNewItem(from: SolutionLanguage?): SolutionLanguage {
            if (from != null)
                return from.copy(name = nextUniqueName(from.name), id = System.currentTimeMillis())
            return AutoCpSettings.getSolutionLanguageTemplate(nextUniqueName("C++"))
        }
    }
    val preferredLangModel = ComboBoxModelDelegate<SolutionLanguage>(popListModel.listModel)

}