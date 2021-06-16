package settings

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import plugin.settings.SolutionLanguage
import ui.poplist.PopList
import ui.poplist.PopListModel
import java.awt.BorderLayout

class LanguagesPanel(popModel: PopListModel<SolutionLanguage>) : PopList<SolutionLanguage>(false, 0.25F, popModel) {
    override val listComponent = JBList<SolutionLanguage>()
    override val listContainer = ToolbarDecorator.createDecorator(listComponent)
        .createPanel()
    override val itemContainer = JBPanelWithEmptyText(BorderLayout()).withEmptyText("Create a New Solution Language")
    override val itemView = LanguageItemAdapter(popModel)
}