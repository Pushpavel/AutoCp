package com.github.pushpavel.autocp.build.settings.ui

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.ui.dsl.DslCallbacks
import com.github.pushpavel.autocp.common.ui.helpers.onSelectedValue
import com.github.pushpavel.autocp.common.ui.layouts.SingleChildContainer
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.CollectionListModel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.SingleSelectionModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
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
            .setAddAction { showNewLangDialog() }
            .setRemoveActionUpdater { sideList.selectedValue?.isDefault == false }
            .createPanel()

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

    var extension = ""

    private fun showNewLangDialog() {
        extension = ""
        val dialog = object : DialogWrapper(false) {
            init {
                title = "Add Language"
                isOKActionEnabled = false
                init()
            }

            override fun createCenterPanel() = panel(LCFlags.fill) {
                row("File Extension") {
                    textField(::extension, 2).withValidationOnInput {
                        run { if (it.text.isBlank()) error("Should not be empty") else null }.also {
                            isOKActionEnabled = it == null
                        }
                    }.focused()
                }
            }
        }

        if (dialog.showAndGet()) {
            val index = langListModel.items.indexOfFirst { it.extension == extension }
            if (index != -1)
                sideList.selectedIndex = index
            else {
                extension = extension.trim().replace(".", "")
                langListModel.add(Lang(extension, null, "",  false))
                sideList.selectedIndex = langListModel.items.size - 1
            }
        }
    }
}