package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.ui.dsl.DslCallbacks
import com.github.pushpavel.autocp.common.ui.helpers.onSelectedValue
import com.github.pushpavel.autocp.common.ui.layouts.SingleChildContainer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import javax.swing.BorderFactory

class LangSettingsPanel : OnePixelSplitter(false, 0.3F), DslCallbacks {
    val langListModel = CollectionListModel<Lang>()

    val sideList: JBList<Lang>

    private val mainContainer: SingleChildContainer

    private val langItemPanel = LangPanel(langListModel)

    init {
        mainContainer = SingleChildContainer("Select a Language", langItemPanel.component)
        sideList = JBList(langListModel).apply {
            selectionModel = SingleSelectionModel()
            cellRenderer = Lang.cellRenderer()
        }

        firstComponent = ToolbarDecorator.createDecorator(sideList)
            .setAddAction {
//                val selectedLanguage = IDELangSelectorDialog(langListModel.items).showAndGetSelection()
//                if (selectedLanguage != null) {
//                    val blank = Lang(selectedLanguage.id, null, null, mutableMapOf())
//                    langListModel.add(blank)
//                    sideList.selectedIndex = langListModel.items.size - 1
//                }
            }.createPanel()

        secondComponent = mainContainer.apply {
            border = BorderFactory.createEmptyBorder(0, 8, 0, 0)
        }


        sideList.onSelectedValue {
            apply()
            langItemPanel.lang = it
            langItemPanel.reset()
            mainContainer.setChildVisible(it != null)
        }
    }

    override fun isModified(): Boolean = langItemPanel.isModified()

    override fun apply() {
        langItemPanel.apply()
        langItemPanel.lang?.let { lang ->
            langListModel.items.indexOfFirst { it.extension == lang.extension }.takeIf { it != -1 }?.let {
                langListModel.setElementAt(lang, it)
            }
        }
    }
}