package settings

import plugin.settings.SolutionLanguage
import settings.base.ComboBoxModelDelegate
import ui.poplist.PopListModel

class SettingsUIModel {
    val popListModel = PopListModel<SolutionLanguage>()
    val preferredLangModel = ComboBoxModelDelegate<SolutionLanguage>(popListModel.listModel)

}