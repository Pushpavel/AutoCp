package settings

import plugin.settings.SolutionLanguage
import ui.poplist.PopList.ItemView
import ui.poplist.PopListModel

class LanguageItemAdapter(popModel: PopListModel<SolutionLanguage>) : ItemView<SolutionLanguage> {

    private val model = LanguageItemModel()
    private val ui = LanguageItemPanel(model)
    override val component = ui.component

    override fun updateView(item: SolutionLanguage) = model.update(item)

    init {
        // TODO: listen model changes and update popModel
    }

}