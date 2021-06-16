package settings

import plugin.settings.SolutionLanguage
import ui.poplist.PopList.ItemView
import ui.poplist.PopListModel

class LanguageItemAdapter(popModel: PopListModel<SolutionLanguage>) : ItemView<SolutionLanguage> {

    private val model = LanguageItemModel(LanguageItemValidator(popModel.listModel))
    private val ui = LanguageItemPanel(model)
    override val component = ui.component

    override fun updateView(item: SolutionLanguage) = model.update(item)

    init {
        model.validChangeByUserListener = { sol ->
            val items = popModel.listModel.items
            val changedIndex = items.indexOfFirst { it.id == sol.id }

            if (changedIndex != -1 && !items[changedIndex].equals(sol))
                popModel.listModel.setElementAt(sol, changedIndex)
        }
    }

}