package settings.langSettings.ui

import com.intellij.ui.CollectionListModel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import settings.langSettings.model.Lang
import settings.langSettings.ui.dialogs.IDELangSelectorDialog
import ui.dsl.DslCallbacks
import ui.helpers.onSelectedValue
import ui.layouts.SingleChildContainer
import javax.swing.BorderFactory

class LangSettingsPanel : OnePixelSplitter(false, 0.3F), DslCallbacks {
    val langListModel = CollectionListModel<Lang>()

    val sideList: JBList<Lang>

    private val mainContainer: SingleChildContainer

    private val langItemPanel = LangItemPanel()

    init {
        mainContainer = SingleChildContainer("Select a Language", langItemPanel.dialogPanel)

        sideList = JBList(langListModel).apply {
            selectionModel = SingleSelectionModel()
            cellRenderer = Lang.cellRenderer()
        }

        firstComponent = ToolbarDecorator.createDecorator(sideList)
            .setAddAction {
                val selectedLanguage = IDELangSelectorDialog(langListModel.items).showAndGetSelection()
                if (selectedLanguage != null) {
                    val blank = Lang(selectedLanguage.id, null, null, mutableListOf())
                    langListModel.add(blank)
                    sideList.selectedIndex = langListModel.items.size - 1
                }
            }.createPanel()

        secondComponent = mainContainer.apply {
            border = BorderFactory.createEmptyBorder(0, 8, 0, 0)
        }


        sideList.onSelectedValue {
            langItemPanel.selectedLang = selectedValue
            langItemPanel.reset()
            mainContainer.setChildVisible(it != null)
        }
    }

    override fun isModified(): Boolean = langItemPanel.isModified()

    override fun apply() {
        langItemPanel.apply()
        sideList.setSelectedValue(langItemPanel.selectedLang, false)
    }
}